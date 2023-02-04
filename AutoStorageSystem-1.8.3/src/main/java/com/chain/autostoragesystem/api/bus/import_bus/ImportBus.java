package com.chain.autostoragesystem.api.bus.import_bus;

import com.chain.autostoragesystem.api.bus.AbstractBus;
import com.chain.autostoragesystem.api.bus.import_filters.EmptyImportFilters;
import com.chain.autostoragesystem.api.bus.import_filters.IImportFilters;
import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import net.minecraft.core.BlockPos;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImportBus extends AbstractBus {

    @Nonnull
    private IImportFilters importFilters = new EmptyImportFilters();
    @Nonnull
    private List<ImportBusInventory> inventories = new ArrayList<>();


    public ImportBus(@Nonnull BlockPos pos) {
        super(pos);
    }

    @Override
    public void tick() {
        for (ImportBusInventory inventory : inventories) {
            inventory.tick();
        }
    }

    @Override
    protected void onInventoriesUpdated(@Nonnull List<IItemHandler> connectedInventories) {
        this.inventories = connectedInventories.stream()
                .map(iItemHandler -> new ImportBusInventory(iItemHandler, importFilters, storageController))
                .collect(Collectors.toList());
    }

    @Override
    protected void onStorageControllerUpdated(@Nonnull IItemsReceiver storageController) {
        this.inventories.forEach(importBusInventory -> importBusInventory.setStorageController(storageController));
    }

    //todo вынести метод в родительский класс? Можно ли обобщить фильтры импорта, экспорта и хранения?
    public void setImportFilters(@Nonnull IImportFilters importFilters) {
        this.importFilters = importFilters;
        onImportFiltersUpdated(this.importFilters);
    }

    protected void onImportFiltersUpdated(@Nonnull IImportFilters importFilters) {
        this.inventories.forEach(importBusInventory -> importBusInventory.setImportFilters(importFilters));
    }
}
