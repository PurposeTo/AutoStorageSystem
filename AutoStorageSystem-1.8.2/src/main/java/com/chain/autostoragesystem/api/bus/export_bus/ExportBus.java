package com.chain.autostoragesystem.api.bus.export_bus;

import com.chain.autostoragesystem.api.bus.AbstractBus;
import com.chain.autostoragesystem.api.bus.IConfigurable;
import com.chain.autostoragesystem.api.bus.filters.IItemTypeFilters;
import com.chain.autostoragesystem.api.storage_system.Config;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.items_transmitter.IItemsTransmitter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class ExportBus extends AbstractBus<ExportInventory> implements IExportBus, IConfigurable {
    @Nonnull
    private final IItemsTransmitter itemsTransmitter;
    @Nonnull
    private final IItemTypeFilters filters;

    public ExportBus(@Nonnull IItemsTransmitter itemsTransmitter,
                     @Nonnull IItemTypeFilters filters) {

        this.itemsTransmitter = itemsTransmitter;
        this.filters = filters;
    }

    public void tick() {
        for (ExportInventory inventory : inventories) {
            inventory.tick();
        }
    }

    @Override
    protected ExportInventory createNewT(@NotNull IItemHandlerWrapper inventory, @NotNull Config config) {
        return new ExportInventory(inventory, itemsTransmitter, filters, config);
    }
}
