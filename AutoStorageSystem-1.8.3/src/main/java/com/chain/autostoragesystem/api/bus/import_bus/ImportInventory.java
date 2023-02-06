package com.chain.autostoragesystem.api.bus.import_bus;

import com.chain.autostoragesystem.api.ProgressManager;
import com.chain.autostoragesystem.api.bus.filters.IItemTypeFilters;
import com.chain.autostoragesystem.api.bus.filters.ItemTypeFiltersFactory;
import com.chain.autostoragesystem.api.storage_system.Config;
import com.chain.autostoragesystem.api.wrappers.item_handler.ItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import lombok.Setter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;


public class ImportInventory {

    private final ItemHandlerWrapper inventory;
    private final ProgressManager progressManager;

    @Setter
    private IItemsReceiver itemsReceiver;
    @Setter
    private IItemTypeFilters filters = ItemTypeFiltersFactory.getForImportBus();
    private Config config = Config.getDefault();

    public ImportInventory(@Nonnull IItemHandler inventory,
                           @Nonnull IItemsReceiver itemsReceiver) {
        this.inventory = new ItemHandlerWrapper(inventory);
        this.itemsReceiver = itemsReceiver;
        this.progressManager = new ProgressManager(config.getPauseIntervalTicks(), this::doImport);
    }

    public void tick() {
        progressManager.tick();
    }

    public void setConfig(Config config) {
        this.config = config;
        progressManager.setDelay(config.getPauseIntervalTicks());
    }

    private void doImport() {
        int capacity = config.getMaxItemsPerOperation();

        List<Item> itemTypesToCheck = inventory.getItemTypes();
        List<IStackInSlot> invalidStacks = inventory.getNotEmptyStacks()
                .stream()
                .filter(stackInSlot -> !filters.canHoldItem(stackInSlot.getItem()))
                .toList();

        for (int i = 0; i < invalidStacks.size() && capacity > 0 && !itemTypesToCheck.isEmpty(); i++) {
            IStackInSlot stackInSlot = invalidStacks.get(i);
            Item itemType = stackInSlot.getItem();

            if (!itemTypesToCheck.contains(itemType)) {
                continue;
            }

            final int itemsCount = stackInSlot.getCount();

            ItemStack remaining = stackInSlot.moveItemStack(capacity, itemsReceiver);

            final int remainingItemsCount = remaining.getCount();
            final int importItemsCount = itemsCount - remainingItemsCount;

            // Изменить кол-во предметов, которые возможно импортировать
            capacity -= importItemsCount;

            // Если остаток не пустой, значит больше не получается больше импортировать данный тип предметов
            if (!remaining.isEmpty()) {
                itemTypesToCheck.remove(itemType);
            }
        }
    }
}
