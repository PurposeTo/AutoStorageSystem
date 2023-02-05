package com.chain.autostoragesystem.api.wrappers;

import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.StackInSlot;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemHandlerGroup implements IItemHandlerWrapper {
    private final List<IItemHandlerWrapper> list;

    public ItemHandlerGroup(@Nonnull List<IItemHandlerWrapper> list) {
        this.list = list;
    }

    @NotNull
    @Override
    public List<StackInSlot> getStacks() {
        return list.stream()
                .flatMap(it -> it.getStacks().stream())
                .toList();
    }

    @NotNull
    @Override
    public List<IStackInSlot> getNotEmptyStacks() {
        return list.stream()
                .flatMap(it -> it.getNotEmptyStacks().stream())
                .toList();
    }

    @NotNull
    @Override
    public List<IStackInSlot> getEmptyStacks() {
        return list.stream()
                .flatMap(it -> it.getEmptyStacks().stream())
                .toList();
    }

    @Override
    public ItemStack moveItemStack(StackInSlot toMove, int toMoveCount, IItemsReceiver itemsReceiver) {
        return null;
    }

    @Override
    public boolean isFull() {
        return list.stream()
                .allMatch(IItemHandlerWrapper::isFull);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        throw new NotImplementedException();
    }

    @Override
    public ItemStack insertItem(@NotNull ItemStack itemStack, boolean simulate) {
        ItemStack remaining = itemStack;

        for (IItemHandlerWrapper inventory : list) {
            if (remaining.isEmpty()) {
                break;
            }

            remaining = inventory.insertItem(remaining, simulate);
        }

        return remaining;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack itemStack, boolean simulate) {
        throw new NotImplementedException();
    }
}
