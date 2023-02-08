package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.api.NeighborsApi;
import com.chain.autostoragesystem.api.bus.import_bus.ImportBus;
import com.chain.autostoragesystem.api.wrappers.ItemHandlerGroup;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.item_handler.ItemHandlerWrapper;
import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.utils.minecraft.Levels;
import com.chain.autostoragesystem.utils.minecraft.TimeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Блок, управляющий системой
 */
public class SystemControllerEntity extends BlockEntity {
    //    private final ProgressManager progressManager;
    private final int operationCapacity = 5;
    private final int totalOperationSpeed = Math.max(Math.round(0.25f * TimeUtil.TICKS_PER_SECOND), 1);

    List<IItemHandlerWrapper> storages = new ArrayList<>();

    List<ImportBus> importBuses = new ArrayList<>();


    public SystemControllerEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.SYSTEM_CONTROLLER_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);

//        this.progressManager = new ProgressManager(totalOperationSpeed, this::updateConnections);
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

        blockEntity.updateConnections(level, pos);

//        blockEntity.progressManager.tick();
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

    private void updateConnections(Level level, BlockPos pos) {
        List<IItemHandlerWrapper> connectedInventories = NeighborsApi.getItemHandlers(level, pos)
                .stream()
                .map(it -> (IItemHandlerWrapper) new ItemHandlerWrapper(it))
                .toList();
        ItemHandlerGroup itemHandlerGroup = new ItemHandlerGroup(connectedInventories);

        var connectedImportBusses = NeighborsApi.getCapabilities(level, pos, ModCapabilities.IMPORT_BUS_CAPABILITY);
        for (var importBus : connectedImportBusses) {
            importBus.setItemsReceiver(itemHandlerGroup);
        }

        var connectedExportBussed = NeighborsApi.getCapabilities(level, pos, ModCapabilities.EXPORT_BUS_CAPABILITY);
        for (var exportBus : connectedExportBussed) {
            exportBus.setItemsTransmitter(itemHandlerGroup);
        }

    }

}
