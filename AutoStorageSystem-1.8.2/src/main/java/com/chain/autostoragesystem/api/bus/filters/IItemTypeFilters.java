package com.chain.autostoragesystem.api.bus.filters;

import net.minecraft.world.item.Item;

import javax.annotation.Nonnull;
import java.util.Set;

public interface IItemTypeFilters {
    /**
     * Может ли в инвентаре находиться данный предмет
     */
    default boolean canHoldItem(@Nonnull Item item) {
        return getItemTypesFilters().contains(item);
    }

    /**
     * Список типов предметов, которые могут находиться в инвентаре
     */
    Set<Item> getItemTypesFilters();
}
