package com.chain.autostoragesystem.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Objects;

public class DefaultItem extends Item {

    public DefaultItem(CreativeModeTab tab) {
        super(new Item.Properties().tab(Objects.requireNonNull(tab)));
    }
}
