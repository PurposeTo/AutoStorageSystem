package com.chain.autostoragesystem.api.bus.export_bus;

import com.chain.autostoragesystem.api.ProgressManager;
import com.chain.autostoragesystem.api.bus.filters.IItemTypeFilters;
import com.chain.autostoragesystem.api.bus.filters.ItemTypeFiltersFactory;
import com.chain.autostoragesystem.api.storage_system.Config;
import com.chain.autostoragesystem.api.wrappers.item_handler.ItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.items_transmitter.IItemsTransmitter;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import lombok.Setter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class ExportInventory {

    private final ItemHandlerWrapper inventory;
    private final ProgressManager progressManager;

    @Setter
    private IItemsTransmitter itemsTransmitter;
    @Setter
    private IItemTypeFilters filters = ItemTypeFiltersFactory.getIronOreForExportBus();
    private Config config = Config.getDefault();

    public ExportInventory(@Nonnull IItemHandler inventory,
                           @Nonnull IItemsTransmitter itemsTransmitter) {
        this.inventory = new ItemHandlerWrapper(inventory);
        this.itemsTransmitter = itemsTransmitter;
        this.progressManager = new ProgressManager(config.getPauseIntervalTicks(), this::doExport);
    }

    public void tick() {
        progressManager.tick();
    }

    public void setConfig(Config config) {
        this.config = config;
        progressManager.setDelay(config.getPauseIntervalTicks());
    }

    private void doExport() {
        int capacity = config.getMaxItemsPerOperation();

        List<Item> itemTypesToCheck = itemsTransmitter.getItemTypes();
        List<IStackInSlot> validStacks = itemsTransmitter.getNotEmptyStacks()
                .stream()
                .filter(stackInSlot -> filters.canHoldItem(stackInSlot.getItem()))
                .toList();

        for (int i = 0; i < validStacks.size() && capacity > 0 && !itemTypesToCheck.isEmpty(); i++) {
            IStackInSlot stackInSlot = validStacks.get(i);
            Item itemType = stackInSlot.getItem();

            if (!itemTypesToCheck.contains(itemType)) {
                continue;
            }

            final int itemsCount = stackInSlot.getCount();

            ItemStack remaining = stackInSlot.moveItemStack(capacity, inventory);

            final int remainingItemsCount = remaining.getCount();
            final int importItemsCount = itemsCount - remainingItemsCount;

            // Изменить кол-во предметов, которые возможно экспортировать
            capacity -= importItemsCount;

            // Если остаток не пустой, значит больше не получается больше экспортировать данный тип предметов
            if (!remaining.isEmpty()) {
                itemTypesToCheck.remove(itemType);
            }
        }
    }
}
