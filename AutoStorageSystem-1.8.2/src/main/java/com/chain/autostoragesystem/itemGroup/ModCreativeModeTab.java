package com.chain.autostoragesystem.itemGroup;

import com.chain.autostoragesystem.item.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {

    public static final CreativeModeTab MODE_TAB = new CreativeModeTab("Auto storage system") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.CITRINE.get());
        }
    };
}
