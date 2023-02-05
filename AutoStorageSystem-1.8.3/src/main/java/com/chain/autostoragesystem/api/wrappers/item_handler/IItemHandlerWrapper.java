package com.chain.autostoragesystem.api.wrappers.item_handler;

import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.StackInSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface IItemHandlerWrapper extends IItemsReceiver {

    /**
     * Get items types in inventory
     * You can modify returned collection
     *
     * @return list of item types in inventory
     */
    @Nonnull
    default List<Item> getItemTypes() {
        return new ArrayList<>(getNotEmptyStacks()
                .stream()
                .collect(Collectors.groupingBy(IStackInSlot::getItem))
                .keySet());
    }

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
