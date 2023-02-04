package com.chain.autostoragesystem.api.wrappers.stack_in_slot;

import com.chain.autostoragesystem.api.wrappers.ItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;


/**
 * Описывает весь стак предметов, лежащих в инвентаре.
 */
public class StackInSlot implements IStackInSlot {
    private final ItemHandlerWrapper itemHandler;
    private final int slot;

    public StackInSlot(@Nonnull ItemHandlerWrapper itemHandler, int slot) {
        ItemStack stack = itemHandler.getStackInSlot(slot);

        this.itemHandler = itemHandler;
        this.slot = slot;
    }

    public ItemStack moveItemStack(final int toMoveCount, final IItemsReceiver itemsReceiver) {
        return this.itemHandler.moveItemStack(this, toMoveCount, itemsReceiver);
    }

    public int getCount() {
        return getItemStack().getCount();
    }

    /**
     * see {@link IItemHandler#getStackInSlot(int)}
     **/
    public ItemStack getItemStack() {
        return this.itemHandler.getStackInSlot(slot);
    }

    /**
     * see {@link IItemHandler#extractItem(int, int, boolean)}
     **/
    @NotNull
    public ItemStack extractItemStack(boolean simulate) {
        int count = getCount();
        return extractItemStack(count, simulate);
    }

    /**
     * see {@link IItemHandler#extractItem(int, int, boolean)}
     **/
    @NotNull
    public ItemStack extractItemStack(int amount, boolean simulate) {
        return this.itemHandler.extractItem(this.slot, amount, simulate);
    }

    /**
     * see {@link IItemHandler#insertItem(int, ItemStack, boolean)}
     **/
    @NotNull
    public ItemStack insertItem(@NotNull ItemStack stack, boolean simulate) {
        return this.itemHandler.insertItem(this.slot, stack, simulate);
    }
}
