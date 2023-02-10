package com.chain.autostoragesystem.api.bus.import_bus;

import com.chain.autostoragesystem.api.bus.AbstractBus;
import com.chain.autostoragesystem.api.bus.IConfigurable;
import com.chain.autostoragesystem.api.bus.filters.IItemTypeFilters;
import com.chain.autostoragesystem.api.storage_system.Config;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class ImportBus extends AbstractBus<ImportInventory> implements IImportBus, IConfigurable {

    @Nonnull
    private final IItemsReceiver itemsReceiver;

    @Nonnull
    private final IItemTypeFilters filters;

    public ImportBus(@Nonnull IItemsReceiver itemsReceiver,
                     @Nonnull IItemTypeFilters filters) {

        this.itemsReceiver = itemsReceiver;
        this.filters = filters;
    }

    @Override
    public void tick() {
        for (ImportInventory inventory : inventories) {
            inventory.tick();
        }
    }

    @Override
    protected ImportInventory createNewT(@NotNull IItemHandlerWrapper inventory, @NotNull Config config) {
        return new ImportInventory(inventory, itemsReceiver, filters, config);
    }
}
