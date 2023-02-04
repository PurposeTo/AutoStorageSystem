package com.chain.autostoragesystem.api.wrappers.item_handler;

import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.StackInSlot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public interface IItemHandlerWrapper extends IItemsReceiver {


    @Nonnull
    List<StackInSlot> getStacks();

    @Nonnull
    List<IStackInSlot> getNotEmptyStacks();

    @Nonnull
    List<IStackInSlot> getEmptyStacks();

    ItemStack moveItemStack(final StackInSlot toMove, final int toMoveCount, final IItemsReceiver itemsReceiver);

    boolean isFull();


    @Nonnull
    ItemStack extractItem(int slot, int amount, boolean simulate);
}
