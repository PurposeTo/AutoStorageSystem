package com.chain.autostoragesystem.api.wrappers.stack_in_slot;

import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;


/**
 * Описывает весь стак предметов, лежащих в инвентаре.
 */
public class StackInSlot implements IStackInSlot {
    private final IItemHandler itemHandler;
    private final int slot;

    public StackInSlot(@Nonnull IItemHandler itemHandler, int slot) {
        this.itemHandler = itemHandler;
        this.slot = slot;
    }

    @Override
    public boolean isEmpty() {
        return unwrap().isEmpty();
    }

    @Override
    public int getCount() {
        return unwrap().getCount();
    }

    @Override
    public @NotNull Item getItem() {
        return unwrap().getItem();
    }

    @Override
    public int getSlot() {
        return this.slot;
    }

    /**
     * see {@link IItemHandler#getStackInSlot(int)}
     **/
    @Override
    public @NotNull ItemStack unwrap() {
        return this.itemHandler.getStackInSlot(slot);
    }

    /**
     * see {@link IItemHandler#extractItem(int, int, boolean)}
     **/
    @NotNull
    @Override
    public ItemStack extractItemStack(boolean simulate) {
        int count = getCount();
        return extractItemStack(count, simulate);
    }

    /**
     * see {@link IItemHandler#extractItem(int, int, boolean)}
     **/
    @NotNull
    @Override
    public ItemStack extractItemStack(int amount, boolean simulate) {
        return this.itemHandler.extractItem(this.slot, amount, simulate);
    }

    /**
     * see {@link IItemHandler#insertItem(int, ItemStack, boolean)}
     **/
    @NotNull
    @Override
    public ItemStack insertItem(@NotNull ItemStack stack, boolean simulate) {
        return this.itemHandler.insertItem(this.slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack moveItemStack(final IItemsReceiver itemsReceiver) {
        int count = getCount();
        return moveItemStack(count, itemsReceiver);
    }

    @Override
    public @NotNull ItemStack moveItemStack(final int toMoveCount, final IItemsReceiver itemsReceiver) {
        ItemStack toMoveStack = this.extractItemStack(toMoveCount, false);
        ItemStack remainingStack = itemsReceiver.insertItem(toMoveStack, false);

        if (!remainingStack.isEmpty()) {
            this.insertItem(remainingStack, false);
        }

        return remainingStack;
    }
}
