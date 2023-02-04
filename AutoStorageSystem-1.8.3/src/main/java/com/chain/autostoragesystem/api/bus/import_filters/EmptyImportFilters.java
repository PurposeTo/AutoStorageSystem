package com.chain.autostoragesystem.api.bus.import_filters;

import net.minecraft.world.item.Item;

public class EmptyImportFilters implements IImportFilters {

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
