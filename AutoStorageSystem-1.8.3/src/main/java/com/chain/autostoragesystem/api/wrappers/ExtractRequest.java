package com.chain.autostoragesystem.api.wrappers;

import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ExtractRequest {
    private final IStackInSlot inventoryStack;
    private final int countToExtract;

    public ExtractRequest(IStackInSlot inventoryStack, int countToExtract) {
        this.inventoryStack = inventoryStack;
        this.countToExtract = countToExtract;
    }

    public ExtractRequest(IStackInSlot inventoryStack) {
        this(inventoryStack, inventoryStack.getCount());
    }

    public ItemStack moveItemStack(final IItemsReceiver itemsReceiver) {
        return this.inventoryStack.moveItemStack(this.countToExtract, itemsReceiver);
    }

    public Item getItemType() {
        return this.inventoryStack.getItemStack().getItem();
    }
}
