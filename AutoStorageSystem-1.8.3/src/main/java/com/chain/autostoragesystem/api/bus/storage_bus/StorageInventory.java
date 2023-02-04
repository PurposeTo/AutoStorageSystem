package com.chain.autostoragesystem.api.bus.storage_bus;

import com.chain.autostoragesystem.api.bus.filters.IInventoryFilters;
import com.chain.autostoragesystem.api.wrappers.item_handler.ItemHandlerWrapper;
import lombok.Setter;

import javax.annotation.Nonnull;

public class StorageInventory {

    private final ItemHandlerWrapper inventory;

    @Setter
    private IInventoryFilters importFilters;

    public StorageInventory(@Nonnull ItemHandlerWrapper inventory, @Nonnull IInventoryFilters importFilters) {
        this.inventory = inventory;
        this.importFilters = importFilters;
    }
}
