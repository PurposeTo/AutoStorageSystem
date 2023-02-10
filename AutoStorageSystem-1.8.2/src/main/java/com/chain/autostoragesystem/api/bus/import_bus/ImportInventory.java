package com.chain.autostoragesystem.api.bus.import_bus;

import com.chain.autostoragesystem.api.ProgressManager;
import com.chain.autostoragesystem.api.bus.IConfigurable;
import com.chain.autostoragesystem.api.bus.filters.IItemTypeFilters;
import com.chain.autostoragesystem.api.storage_system.Config;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerComparer;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import lombok.Setter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;


public class ImportInventory implements IItemHandlerComparer, IConfigurable {

    @Nonnull
    private final IItemHandlerWrapper inventory;
    @Setter
    private IItemsReceiver itemsReceiver;

    @Nonnull
    private final IItemTypeFilters filters;

    @Nonnull
    private Config config;
    @Nonnull
    private final ProgressManager progressManager;

    public ImportInventory(@Nonnull IItemHandlerWrapper inventory,
                           @Nonnull IItemsReceiver itemsReceiver,
                           @Nonnull IItemTypeFilters filters,
                           @Nonnull Config config) {
        this.inventory = inventory;
        this.itemsReceiver = itemsReceiver;
        this.filters = filters;
        this.config = config;
        this.progressManager = new ProgressManager(config.getPauseIntervalTicks(), this::doImport);
    }

    public void tick() {
        progressManager.tick();
    }

    @Override
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

    @Override
    public boolean same(@NotNull IItemHandlerWrapper itemHandler) {
        return this.inventory.same(itemHandler);
    }

}
