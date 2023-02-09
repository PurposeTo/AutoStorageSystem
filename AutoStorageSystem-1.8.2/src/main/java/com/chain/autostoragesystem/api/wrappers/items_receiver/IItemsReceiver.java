package com.chain.autostoragesystem.api.wrappers.items_receiver;

import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerComparer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public interface IItemsReceiver extends IItemHandlerComparer {
    ItemStack insertItem(@Nonnull final ItemStack itemStack, final boolean simulate);

    @Nonnull
    ItemStack insertItem(int slot, @Nonnull ItemStack itemStack, boolean simulate);
}
