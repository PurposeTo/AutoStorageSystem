package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.api.IImportBus;
import com.chain.autostoragesystem.api.ImportRequest;
import com.chain.autostoragesystem.api.ProgressManager;
import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.utils.ChatUtil;
import com.chain.autostoragesystem.utils.minecraft.Levels;
import com.chain.autostoragesystem.utils.minecraft.TimeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
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
    int operationCapacity = 1;
    private int totalOperationSpeed;

    List<Object> storages; // can import or export to storage. Items visible in SystemMaster menu.

    public Player player; // дебаг поле - вместо шины экспорта

    // LinkedHashSet чтобы не хранить дубликаты и иметь очередность
    // lazy, чтобы сам SystemControllerEntity мог удалить из списка тот, который был разрушен
    LinkedHashSet<LazyOptional<IImportBus>> importBusesOps = new LinkedHashSet<>(); // can only import from these storages. Items don't visible in SystemMaster menu.

    List<Object> exportStorages; // can only export to these storages. Items don't visible in SystemMaster menu.


    public SystemControllerEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.SYSTEM_CONTROLLER_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);

        this.totalOperationSpeed = 2 * TimeUtil.TICKS_PER_SECOND;
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


    // положить предмет из одного сундука в другой
    //1. узнать, требуется ли положить куда-то предмет. Узнать количество требуемых элементов.
    // (Все что есть, N-е количество и т.д. Зависит как от настроек кабеля, так и от вместимости инвентаря, т.е. количества свободных слотов)
    //todo blacklist, whitelist фильтры, без фильтров. Приоритет загрузки в инвентарь.

    //2. если да, то найти хранилище, где этот предмет есть.

    //3. если такие хранилища найдены, то забрать из хранилищ
    // "количество, не больше чем operationCapacity И не больше, чем требуется"

    //4. положить забранные предметы в инвентарь из шага 1.
    private void doImport() {
        this.getImportRequests()
                .stream()
                .findFirst()
                .ifPresent(importRequest -> {
                    ItemStack itemStack = importRequest.execute();
                    this.player.getInventory().add(itemStack);
                });
    }

    private LinkedHashSet<ImportRequest> getImportRequests() {
        return importBusesOps.stream()
                .flatMap(it -> it.resolve().stream())
                .flatMap(importBus -> importBus.getImportRequests().stream())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
