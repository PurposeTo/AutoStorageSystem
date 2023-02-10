package com.chain.autostoragesystem.api.bus.export_bus;

import com.chain.autostoragesystem.api.ProgressManager;
import com.chain.autostoragesystem.api.bus.IConfigurable;
import com.chain.autostoragesystem.api.bus.filters.IItemTypeFilters;
import com.chain.autostoragesystem.api.storage_system.Config;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerComparer;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.items_transmitter.IItemsTransmitter;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class ExportInventory implements IItemHandlerComparer, IConfigurable {

    @Nonnull
    private final IItemsReceiver inventory;
    @Nonnull
    private final IItemsTransmitter itemsTransmitter;
    @Nonnull
    private final IItemTypeFilters filters;

    @Nonnull
    private Config config;
    @Nonnull
    private final ProgressManager progressManager;

    public ExportInventory(@Nonnull IItemsReceiver inventory,
                           @Nonnull IItemsTransmitter itemsTransmitter,
                           @Nonnull IItemTypeFilters filters,
                           @Nonnull Config config) {
        this.inventory = inventory;
        this.itemsTransmitter = itemsTransmitter;
        this.filters = filters;
        this.config = config;
        this.progressManager = new ProgressManager(config.getPauseIntervalTicks(), this::doExport);
    }

    public void tick() {
        progressManager.tick();
    }

    @Override
    public void setConfig(@Nonnull Config config) {
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

    @Override
    public boolean same(@NotNull IItemHandlerWrapper itemHandler) {
        return this.inventory.same(itemHandler);
    }
}
