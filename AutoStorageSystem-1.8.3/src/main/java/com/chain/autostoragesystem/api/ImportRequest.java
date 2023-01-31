package com.chain.autostoragesystem.api;

import lombok.Getter;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;


@Getter
public class ImportRequest {

    private final IItemHandler inventory; // откуда изъять
    private final ItemStack requestStack; // что изъять. Don't modify this!
    private final int requestSlot; // из какого слота изъять

    public ImportRequest(@Nonnull IItemHandler inventory,
                         @Nonnull ItemStack requestStack,
                         int requestSlot) {
        this.inventory = inventory;
        this.requestStack = requestStack;
        this.requestSlot = requestSlot;
    }

    public ItemStack execute() {

        //todo change amount
        return inventory.extractItem(requestSlot, 1, false);
    }

    @Override
    public String toString() {
        return String.format("""
                ImportRequest:
                requestStack = %s,
                requestSlot = %s""", requestStack, requestSlot);
    }
}
