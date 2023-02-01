package com.chain.autostoragesystem.utils.minecraft;

import net.minecraft.world.item.ItemStack;

public class ItemStackUtil {

    /**
     * Полный ли стак предметов
     */
    public static boolean isFull(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }

        if (!itemStack.isStackable()) {
            return true;
        }

        return haveMaxStackSize(itemStack);
    }

    public static boolean canAccept(ItemStack from, ItemStack to) {
        if (from.isEmpty() || to.isEmpty()) {
            return true;
        }

        if (!from.isStackable() || !to.isStackable()) {
            return false;
        }

        boolean sameItem = to.sameItem(from);
        if (!sameItem) {
            return false;
        }

        return !haveMaxStackSize(to);
    }

    public static boolean haveMaxStackSize(ItemStack itemStack) {
        int maxStackSize = itemStack.getMaxStackSize();
        int stackSize = itemStack.getCount();

        return stackSize == maxStackSize;

    }

    /**
     * Равен ли остаток от стака самому стаку
     */
    public static boolean equalsStackWithRemaining(ItemStack first, ItemStack second) {
        return first.sameItem(second) &&
                first.getCount() == second.getCount();
    }
}
