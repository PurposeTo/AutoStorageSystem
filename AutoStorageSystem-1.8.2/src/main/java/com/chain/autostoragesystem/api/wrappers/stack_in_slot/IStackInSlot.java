package com.chain.autostoragesystem.api.wrappers.stack_in_slot;

import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public interface IStackInSlot {

    default boolean notEmpty() {
        return !isEmpty();
    }

    boolean isEmpty();

    int getCount();

    @NotNull
    Item getItem();

    int getSlot();

    @NotNull
    ItemStack unwrap();

    /**
     * see {@link IItemHandler#extractItem(int, int, boolean)}
     **/
    @NotNull
    ItemStack extractItemStack(boolean simulate);

    /**
     * see {@link IItemHandler#extractItem(int, int, boolean)}
     **/
    @NotNull
    ItemStack extractItemStack(int amount, boolean simulate);

    /**
     * see {@link IItemHandler#insertItem(int, ItemStack, boolean)}
     **/
    @NotNull
    ItemStack insertItem(@NotNull ItemStack stack, boolean simulate);

    @NotNull
    ItemStack moveItemStack(final IItemsReceiver itemsReceiver);

    @NotNull
    ItemStack moveItemStack(final int toMoveCount, final IItemsReceiver itemsReceiver);
}
