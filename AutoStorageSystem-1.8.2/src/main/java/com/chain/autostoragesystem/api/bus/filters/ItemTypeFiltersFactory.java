package com.chain.autostoragesystem.api.bus.filters;

import com.chain.autostoragesystem.api.wrappers.container.ContainerWrapper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashSet;
import java.util.Set;

public class ItemTypeFiltersFactory {


    public static IItemTypeFilters getFromContainer(Container container) {
        return new IItemTypeFilters() {
            @Override
            public Set<Item> getItemTypesFilters() {

                return new ContainerWrapper(container).getItemTypes();
            }
        };
    }


    public static IItemTypeFilters getForImportBus() {
        return new IItemTypeFilters() {
            @Override
            public Set<Item> getItemTypesFilters() {
                return new HashSet<>();
            }
        };
    }

    public static IItemTypeFilters getIronOreForExportBus() {
        return new IItemTypeFilters() {
            @Override
            public Set<Item> getItemTypesFilters() {
                return Set.of(Items.IRON_ORE);
            }
        };
    }
}
