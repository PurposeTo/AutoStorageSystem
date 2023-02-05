package com.chain.autostoragesystem.api.bus.filters;

import net.minecraft.world.item.Item;

import javax.annotation.Nonnull;

public interface IItemTypeFilters {
    /**
     * Может ли в инвентаре находиться данный предмет
     */
    boolean canHoldItem(@Nonnull Item item);
}
