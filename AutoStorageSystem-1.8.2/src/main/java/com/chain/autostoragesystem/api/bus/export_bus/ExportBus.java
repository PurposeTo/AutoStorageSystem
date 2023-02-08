package com.chain.autostoragesystem.api.bus.export_bus;

import com.chain.autostoragesystem.api.bus.AbstractBus;
import com.chain.autostoragesystem.api.wrappers.items_transmitter.EmptyItemsTransmitter;
import com.chain.autostoragesystem.api.wrappers.items_transmitter.IItemsTransmitter;
import net.minecraft.core.BlockPos;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExportBus extends AbstractBus implements IExportBus {

    @Nonnull
    private List<ExportInventory> inventories = new ArrayList<>();

    @Nonnull
    private IItemsTransmitter itemsTransmitter = new EmptyItemsTransmitter();

    public ExportBus(@Nonnull BlockPos pos) {
        super(pos);
    }

    @Override
    public void tick() {
        for (ExportInventory inventory : inventories) {
            inventory.tick();
        }
    }

    @Override
    protected void onInventoriesUpdated(@Nonnull List<IItemHandler> connectedInventories) {
        this.inventories = connectedInventories.stream()
                .map(iItemHandler -> new ExportInventory(iItemHandler, itemsTransmitter))
                .collect(Collectors.toList());
    }

    @Override
    public void setItemsTransmitter(@Nonnull IItemsTransmitter itemsTransmitter) {
        this.itemsTransmitter = itemsTransmitter;
        onStorageControllerUpdated(this.itemsTransmitter);
    }

    protected void onStorageControllerUpdated(@Nonnull IItemsTransmitter storageController) {
        this.inventories.forEach(importInventory -> importInventory.setItemsTransmitter(storageController));
    }
}
