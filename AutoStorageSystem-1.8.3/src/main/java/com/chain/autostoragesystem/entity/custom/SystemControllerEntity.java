package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.api.ProgressManager;
import com.chain.autostoragesystem.api.bus.import_bus.ImportBus;
import com.chain.autostoragesystem.api.wrappers.item_handler.ItemHandlerWrapper;
import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.utils.minecraft.Levels;
import com.chain.autostoragesystem.utils.minecraft.TimeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    private final int operationCapacity = 5;
    private final int totalOperationSpeed = Math.max(Math.round(0.25f * TimeUtil.TICKS_PER_SECOND), 1);

    List<Object> storages; // can import or export to storage. Items visible in SystemMaster menu.

    // List чтобы не хранить дубликаты и иметь очередность
    // lazy, чтобы сам SystemControllerEntity мог удалить из списка тот, который был разрушен
    //todo точно надо lazy? может сделать отдельно поведение, чтобы удалять тот, что более не валиден?
    List<LazyOptional<ImportBus>> importBusesOps = new ArrayList<>(); // can only import from these storages. Items don't visible in SystemMaster menu.

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

//    public void addImportBus(Player player, LazyOptional<IImportBus> importBus) {
//        if (!importBus.isPresent()) {
//            return;
//        }
//
//        boolean isNew = importBusesOps.add(importBus);
//
//        //log to chat
//        StringBuilder b = new StringBuilder();
//        String addLog = isNew ? "Add!\n" : "Already added!\n";
//        b.append(addLog);
//        getImportRequests().forEach(obj -> b.append(obj).append("\n"));
//        ChatUtil.playerSendMessage(player, b.toString());
//    }
//
//    //todo debug
//    public void addExportBus(Player player, LazyOptional<IImportBus> importBus) {
//        if (exportStorages.isEmpty()) {
//            IItemHandler itemHandler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
//            ItemHandlerWrapper wrapper = new ItemHandlerWrapper(itemHandler);
//            exportStorages.add(wrapper);
//        }
//
//    }

    private void doImport() {
        List<ImportBus> importBuses = importBusesOps.stream()
                .flatMap(it -> it.resolve().stream())
                .toList();
    }

}
