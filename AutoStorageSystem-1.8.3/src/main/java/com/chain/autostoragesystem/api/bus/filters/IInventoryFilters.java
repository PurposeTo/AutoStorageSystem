package com.chain.autostoragesystem.api.bus.filters;

import net.minecraft.world.item.Item;

public interface IInventoryFilters {

    /**
     * Может ли в инвентаре находиться данный предмет
     */
    boolean canHoldItem(Item item);

    boolean hasAmountLimit(Item item);

    int getAmountLimit(Item item);
}
