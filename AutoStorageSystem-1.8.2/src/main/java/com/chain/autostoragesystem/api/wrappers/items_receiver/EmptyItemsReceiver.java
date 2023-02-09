package com.chain.autostoragesystem.api.wrappers.items_receiver;

import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EmptyItemsReceiver implements IItemsReceiver {
    @Override
    public ItemStack insertItem(@NotNull ItemStack itemStack, boolean simulate) {
        return itemStack;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack itemStack, boolean simulate) {
        return itemStack;
    }

    @Override
    public boolean same(@NotNull IItemHandlerWrapper itemHandler) {
        return false;
    }
}

