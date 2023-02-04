package com.chain.autostoragesystem.api.bus;

import com.chain.autostoragesystem.api.bus.filters.EmptyInventoryFilters;
import com.chain.autostoragesystem.api.bus.filters.IInventoryFilters;
import com.chain.autostoragesystem.api.wrappers.items_receiver.EmptyItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import lombok.Getter;
import net.minecraft.core.BlockPos;

import javax.annotation.Nonnull;

public abstract class AbstractBus extends ItemHandlersConnector {

    @Getter
    @Nonnull
    protected final BlockPos pos;

    @Nonnull
    protected IInventoryFilters filters = new EmptyInventoryFilters();

    @Nonnull
    protected IItemsReceiver storageController = new EmptyItemsReceiver();

    protected AbstractBus(@Nonnull BlockPos pos) {
        this.pos = pos;
    }

    public abstract void tick();

    public void setStorageController(@Nonnull IItemsReceiver storageController) {
        this.storageController = storageController;
        onStorageControllerUpdated(this.storageController);
    }

    public void setFilters(@Nonnull IInventoryFilters filters) {
        this.filters = filters;
        onFiltersUpdated(this.filters);
    }

    protected abstract void onStorageControllerUpdated(@Nonnull IItemsReceiver storageController);

    protected abstract void onFiltersUpdated(@Nonnull IInventoryFilters importFilters);
}