package com.chain.autostoragesystem.api.bus.filters;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ItemTypeFiltersFactory {


    public static IItemTypeFilters getForImportBus() {
        return new IItemTypeFilters() {
            @Override
            public List<Item> getItemTypesFilters() {
                return new ArrayList<>();
            }
        };
    }

    public static IItemTypeFilters getIronOreForExportBus() {
        return new IItemTypeFilters() {
            @Override
            public List<Item> getItemTypesFilters() {
                return List.of(Items.IRON_ORE);
            }
        };
    }
}
