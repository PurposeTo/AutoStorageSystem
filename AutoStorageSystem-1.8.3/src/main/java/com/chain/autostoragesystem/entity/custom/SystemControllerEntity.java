package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.api.ItemHandlerWrapper;
import com.chain.autostoragesystem.api.ProgressManager;
import com.chain.autostoragesystem.api.bus.IImportBus;
import com.chain.autostoragesystem.api.bus.ImportRequest;
import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.utils.ChatUtil;
import com.chain.autostoragesystem.utils.minecraft.Levels;
import com.chain.autostoragesystem.utils.minecraft.TimeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Блок, управляющий системой
 */
// Операция = попытка обработки одного запроса импорта И одного запроса экспорта
// Запрос импорта, экспорта = процесс перемещения N-го количества элементов одного типа из одного инвентаря в другой

// Объем операции = максимальное количество элементов одного типа во время выполнения одной операции. Измеряется в int
// Скорость = количество операций за одну секунду. Измеряется в int

//перемещение предметов между storages
/*
Может быть запрос "забрать предметы" - из importStorages (например, из сундука или готовые слитки из печки, готовый предмет из автоверстока).
Откуда - из importStorages.
Куда - в exportStorages или в storages. (Приоритет у exportStorages выше)

Может быть запрос "положить предметы" - в exportStorages (например, в сундук, на плавку в печку, в автоверстак)
Куда - в exportStorages.
Откуда - из importStorages или из storages. (Приоритет у importStorages выше)
 */
//todo должен быть один на всю систему
public class SystemControllerEntity extends BlockEntity {


    private final ProgressManager progressManager;
    private final int operationCapacity = 5; //todo баг: если в инвентарь нужно положить предметов меньше, чем это число, то оно вовсе не кладется.
    private final int totalOperationSpeed = Math.max(Math.round(0.25f * TimeUtil.TICKS_PER_SECOND), 1);

    List<Object> storages; // can import or export to storage. Items visible in SystemMaster menu.

    // List чтобы не хранить дубликаты и иметь очередность
    // lazy, чтобы сам SystemControllerEntity мог удалить из списка тот, который был разрушен
    //todo точно надо lazy? может сделать отдельно поведение, чтобы удалять тот, что более не валиден?
    List<LazyOptional<IImportBus>> importBusesOps = new ArrayList<>(); // can only import from these storages. Items don't visible in SystemMaster menu.

    List<ItemHandlerWrapper> exportStorages = new ArrayList<>(); // can only export to these storages. Items don't visible in SystemMaster menu.


    public SystemControllerEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.SYSTEM_CONTROLLER_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);

        this.progressManager = new ProgressManager(totalOperationSpeed, this::doImport);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, SystemControllerEntity blockEntity) {
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SystemControllerEntity blockEntity) {
        Levels.requireServerSide(level);

        blockEntity.progressManager.increase();
    }

    public void addImportBus(Player player, LazyOptional<IImportBus> importBus) {
        if (!importBus.isPresent()) {
            return;
        }

        boolean isNew = importBusesOps.add(importBus);

        //log to chat
        StringBuilder b = new StringBuilder();
        String addLog = isNew ? "Add!\n" : "Already added!\n";
        b.append(addLog);
        getImportRequests().forEach(obj -> b.append(obj).append("\n"));
        ChatUtil.playerSendMessage(player, b.toString());
    }

    //todo debug
    public void addExportBus(Player player, LazyOptional<IImportBus> importBus) {
        if (exportStorages.isEmpty()) {
            IItemHandler itemHandler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
            ItemHandlerWrapper wrapper = new ItemHandlerWrapper(itemHandler);
            exportStorages.add(wrapper);
        }

    }


    /*
     * Может быть запрос "забрать предметы" - из importStorages (например, из сундука или готовые слитки из печки, готовый предмет из автоверстока).
     * Откуда - из importStorages.
     * Куда - в exportStorages или в storages. (Приоритет у exportStorages выше)
     * todo Запрос выполняется одновременно для всех шин импорта
     *
     *
     * 1. Взять запрос на изъятие предметов
     * 2. Узнать, возможно ли его куда-либо положить (перебрать все инвентари, которые не полностью заполнены)
     * 2.1 Если можно, то положить. Закончить процесс импорта.
     * 2.2 Если нельзя, то попробовать положить в следующий слот
     * 3. Если не нашли слоты, то закончить процесс импорта.
     *
     * Положить ItemStack:
     * 1.1 Если весь размер влезает, то положить.
     * 1.2 Если не весь влезает, то положить ту часть, что влезает.
     * Ту часть, что не влезла, оставить в прежнем инвентаре.
     */
    private void doImport() {
        List<ImportRequest> importRequests = this.getImportRequests();
        boolean anyHandled = handleImport(importRequests);
    }

    /**
     * Обработать хотя бы один (но не более одного)
     * запроса импорта из списка, хотя бы частично
     *
     * @return успешно?
     */
    private boolean handleImport(@Nonnull List<ImportRequest> importRequests) {
        return importRequests.stream().anyMatch(this::handleImport);
    }

    /**
     * Обработать запрос импорта, хотя бы частично
     *
     * @return успешно?
     */
    private boolean handleImport(@Nonnull ImportRequest importRequest) {
        ItemHandlerWrapper transmitter = importRequest.getInventory();
        int transmitterSlot = importRequest.getSlot();

        List<ItemHandlerWrapper> receiveInventories = exportStorages;
        return receiveInventories
                .stream()
                .anyMatch(receiver -> transmitter.moveItemStack(transmitterSlot, operationCapacity, receiver));
    }

    private List<ImportRequest> getImportRequests() {
        return importBusesOps.stream()
                .flatMap(it -> it.resolve().stream())
                .flatMap(importBus -> importBus.getImportRequests().stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<IImportBus> getImportBusses() {
        return this.importBusesOps.stream()
                .flatMap(it -> it.resolve().stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
