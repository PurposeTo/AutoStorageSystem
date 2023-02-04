package com.chain.autostoragesystem.api.bus.import_bus;

import com.chain.autostoragesystem.api.bus.AbstractBus;
import com.chain.autostoragesystem.api.bus.filters.IInventoryFilters;
import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import net.minecraft.core.BlockPos;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImportBus extends AbstractBus {

    @Nonnull
    private List<ImportInventory> inventories = new ArrayList<>();

    public ImportBus(@Nonnull BlockPos pos) {
        super(pos);
    }

    @Override
    public void tick() {
        for (ImportInventory inventory : inventories) {
            inventory.tick();
        }
    }

    @Override
    protected void onInventoriesUpdated(@Nonnull List<IItemHandler> connectedInventories) {
        this.inventories = connectedInventories.stream()
                .map(iItemHandler -> new ImportInventory(iItemHandler, filters, storageController))
                .collect(Collectors.toList());
    }

    @Override
    protected void onStorageControllerUpdated(@Nonnull IItemsReceiver storageController) {
        this.inventories.forEach(importInventory -> importInventory.setStoragesGroup(storageController));
    }

    @Override
    protected void onFiltersUpdated(@Nonnull IInventoryFilters filters) {
        this.inventories.forEach(importInventory -> importInventory.setFilters(filters));
    }
}
