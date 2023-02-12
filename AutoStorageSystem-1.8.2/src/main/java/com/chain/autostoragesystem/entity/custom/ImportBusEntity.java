package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.api.bus.filters.ItemTypeFiltersFactory;
import com.chain.autostoragesystem.api.bus.import_bus.ImportBus;
import com.chain.autostoragesystem.api.connection.Connection;
import com.chain.autostoragesystem.api.wrappers.ItemHandlerGroup;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.entity.custom.base.BaseBlockEntity;
import com.chain.autostoragesystem.utils.minecraft.TickerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImportBusEntity extends BaseBlockEntity implements TickerUtil.TickableServer {
    private final Connection connection = new Connection(this);
    private final SimpleContainer filters = new SimpleContainer(27);

    private final ImportBus importBus;

    private final ItemHandlerGroup itemsReceiver = new ItemHandlerGroup();


    public ImportBusEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.IMPORT_BUS_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);

        importBus = new ImportBus(itemsReceiver, ItemTypeFiltersFactory.getFromContainer(filters));
        registerCapability(ModCapabilities.CONNECTION, () -> connection);
        registerCapability(ModCapabilities.CONTAINER, () -> filters);
        registerCapability(ModCapabilities.IMPORT_BUS, () -> importBus);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        registerCapability(ModCapabilities.CONNECTION, () -> connection);
        registerCapability(ModCapabilities.CONTAINER, () -> filters);
        registerCapability(ModCapabilities.IMPORT_BUS, () -> importBus);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

    @Override
    public void tickServer() {
        List<IItemHandlerWrapper> storageInventories = connection.getStorageBusses()
                .stream()
                .flatMap(connection -> connection.getStorageItemHandlers().stream())
                .toList();
        itemsReceiver.resetItemHandlers(storageInventories);

        List<IItemHandlerWrapper> connectedInventories = connection.getNeighboursItemHandlers();
        importBus.updateInventories(connectedInventories);

        importBus.tick();
    }
}
