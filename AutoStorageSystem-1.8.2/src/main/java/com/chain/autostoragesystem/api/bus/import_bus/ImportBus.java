package com.chain.autostoragesystem.api.bus.import_bus;

import com.chain.autostoragesystem.api.bus.AbstractBus;
import com.chain.autostoragesystem.api.wrappers.items_receiver.EmptyItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import net.minecraft.core.BlockPos;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImportBus extends AbstractBus implements IImportBus {

    @Nonnull
    private List<ImportInventory> inventories = new ArrayList<>();

    @Nonnull
    private IItemsReceiver itemsReceiver = new EmptyItemsReceiver();

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
    public void setItemsReceiver(@Nonnull IItemsReceiver itemsReceiver) {
        this.itemsReceiver = itemsReceiver;
        onStorageControllerUpdated(this.itemsReceiver);
    }

    @Override
    protected void onInventoriesUpdated(@Nonnull List<IItemHandler> connectedInventories) {
        this.inventories = connectedInventories.stream()
                .map(iItemHandler -> new ImportInventory(iItemHandler, itemsReceiver))
                .collect(Collectors.toList());
    }

    protected void onStorageControllerUpdated(@Nonnull IItemsReceiver storageController) {
        this.inventories.forEach(importInventory -> importInventory.setItemsReceiver(storageController));
    }

}
