package com.chain.autostoragesystem.api.wrappers.items_transmitter;

import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.StackInSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EmptyItemsTransmitter implements IItemsTransmitter{
    @NotNull
    @Override
    public List<StackInSlot> getStacks() {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public List<IStackInSlot> getNotEmptyStacks() {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public List<IStackInSlot> getEmptyStacks() {
        return new ArrayList<>();
    }

    @Override
    public ItemStack moveItemStack(StackInSlot toMove, int toMoveCount, IItemsReceiver itemsReceiver) {
        return toMove.resolve();
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }
}
