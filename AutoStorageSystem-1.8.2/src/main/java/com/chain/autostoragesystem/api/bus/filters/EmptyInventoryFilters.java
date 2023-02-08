package com.chain.autostoragesystem.api.bus.filters;

import net.minecraft.world.item.Item;

public class EmptyInventoryFilters implements IInventoryFilters {

    @Override
    public boolean canHoldItem(Item item) {
        return true;
    }

    @Override
    public boolean hasAmountLimit(Item item) {
        return false;
    }

    @Override
    public int getAmountLimit(Item item) {
        return -1;
    }
}
