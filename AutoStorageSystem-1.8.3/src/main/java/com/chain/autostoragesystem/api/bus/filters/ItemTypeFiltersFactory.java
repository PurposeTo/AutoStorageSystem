package com.chain.autostoragesystem.api.bus.filters;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class ItemTypeFiltersFactory {


    public static IItemTypeFilters getForImportBus() {
        return new IItemTypeFilters() {
            @Override
            public boolean canHoldItem(@NotNull Item item) {
                return false;
            }
        };
    }
}
