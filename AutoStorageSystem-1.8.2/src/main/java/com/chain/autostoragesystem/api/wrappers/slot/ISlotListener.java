package com.chain.autostoragesystem.api.wrappers.slot;

import net.minecraft.world.item.ItemStack;

public interface ISlotListener {
    void onChanged(int slotIndex, ItemStack itemStack);
}
