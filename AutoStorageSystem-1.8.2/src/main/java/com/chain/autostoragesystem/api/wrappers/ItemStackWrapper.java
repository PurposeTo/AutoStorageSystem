package com.chain.autostoragesystem.api.wrappers;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemStackWrapper {
    private final ItemStack itemStack;

    public ItemStackWrapper(@Nonnull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack resolve() {
        return itemStack;
    }

    /**
     * Можно ли добавить хотя бы часть стака из ItemStack stack
     */
    public boolean canAccept(ItemStack stack) {
        if (stack.isEmpty() || itemStack.isEmpty()) {
            return true;
        }

        if (!stack.isStackable() || !itemStack.isStackable()) {
            return false;
        }

        boolean sameItem = itemStack.sameItem(stack);
        if (!sameItem) {
            return false;
        }

        return !haveMaxStackSize(itemStack);
    }

    /**
     * Полный ли стак предметов
     */
    public boolean isFull() {
        if (itemStack.isEmpty()) {
            return false;
        }

        if (!itemStack.isStackable()) {
            return true;
        }

        return haveMaxStackSize(itemStack);
    }

    public boolean haveMaxStackSize(ItemStack itemStack) {
        int maxStackSize = itemStack.getMaxStackSize();
        int stackSize = itemStack.getCount();

        return stackSize == maxStackSize;
    }

    /**
     * Равен ли остаток от стака самому стаку
     * Не использовать для сравнения с другими ItemStack, тк данный метод не учитывает
     * параметр damaged
     */
    public boolean equalsStackWithRemaining(ItemStack remaining) {
        return itemStack.sameItem(remaining) &&
                itemStack.getCount() == remaining.getCount();
    }
}
