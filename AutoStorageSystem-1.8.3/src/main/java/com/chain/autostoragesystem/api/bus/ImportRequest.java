package com.chain.autostoragesystem.api.bus;

import com.chain.autostoragesystem.api.ItemHandlerWrapper;
import lombok.Getter;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;


public class ImportRequest {

    @Getter
    private final ItemStack stackInfo; // что изъять. Don't modify this!

    private final IItemHandler inventory; // откуда изъять

    @Getter
    private final int slot; // из какого слота изъять

    public ImportRequest(@Nonnull IItemHandler inventory,
                         @Nonnull ItemStack stackInfo,
                         int slot) {
        this.inventory = inventory;
        this.stackInfo = stackInfo;
        this.slot = slot;
    }

    public ItemHandlerWrapper getInventory() {
        return new ItemHandlerWrapper(inventory);
    }

    public ItemStack execute(int amount) {
        return inventory.extractItem(slot, amount, false);
    }

    @Override
    public String toString() {
        return String.format("""
                ImportRequest:
                requestStack = %s,
                requestSlot = %s""", stackInfo, slot);
    }
}
