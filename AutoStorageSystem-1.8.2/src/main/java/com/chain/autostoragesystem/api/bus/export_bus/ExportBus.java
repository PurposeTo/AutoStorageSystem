package com.chain.autostoragesystem.api.bus.export_bus;

import com.chain.autostoragesystem.api.bus.filters.IItemTypeFilters;
import com.chain.autostoragesystem.api.storage_system.Config;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.items_transmitter.IItemsTransmitter;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class ExportBus implements IExportBus {
    @Nonnull
    private final IItemsTransmitter itemsTransmitter;
    @Nonnull
    private final IItemTypeFilters filters;

    @Nonnull
    private final List<ExportInventory> inventories;
    @Nonnull
    private Config config = Config.getDefault();

    public ExportBus(@Nonnull List<IItemHandlerWrapper> inventories,
                     @Nonnull IItemsTransmitter itemsTransmitter,
                     @Nonnull IItemTypeFilters filters) {

        this.itemsTransmitter = itemsTransmitter;
        this.filters = filters;
        this.inventories = inventories
                .stream()
                .map(itemHandler -> new ExportInventory(itemHandler, itemsTransmitter, filters, config))
                .collect(Collectors.toList());
    }

    public void tick() {
        for (ExportInventory inventory : inventories) {
            inventory.tick();
        }
    }

    public void updateInventories(@Nonnull List<IItemHandlerWrapper> inventories) {
        List<ExportInventory> invalidExportInventories = this.inventories
                .stream()
                .filter(exportInv -> inventories.stream().noneMatch(exportInv::same))
                .toList();
        this.inventories.removeAll(invalidExportInventories);

        var newInventories = inventories
                .stream()
                .filter(itemHandler -> this.inventories.stream().noneMatch(exportInventory -> exportInventory.same(itemHandler)))
                .map(itemHandler -> new ExportInventory(itemHandler, itemsTransmitter, filters, config))
                .toList();
        this.inventories.addAll(newInventories);
    }

    public void setConfig(@Nonnull Config config) {
        this.config = config;
        inventories.forEach(inventory -> inventory.setConfig(config));
    }
}
