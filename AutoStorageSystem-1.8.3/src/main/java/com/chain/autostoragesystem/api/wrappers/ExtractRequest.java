package com.chain.autostoragesystem.api.wrappers;

import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ExtractRequest {
    private final IStackInSlot stackInSlot;
    private final int countToExtract;

    public ExtractRequest(IStackInSlot stackInSlot, int countToExtract) {
        this.stackInSlot = stackInSlot;
        this.countToExtract = countToExtract;
    }

    public ExtractRequest(IStackInSlot stackInSlot) {
        this(stackInSlot, stackInSlot.getCount());
    }

    public ItemStack moveItemStack(final IItemsReceiver itemsReceiver) {
        return this.stackInSlot.moveItemStack(this.countToExtract, itemsReceiver);
    }

    public Item getItemType() {
        return this.stackInSlot.resolve().getItem();
    }

    public IStackInSlot getStackInSlot() {
        return this.getStackInSlot();
    }
}
