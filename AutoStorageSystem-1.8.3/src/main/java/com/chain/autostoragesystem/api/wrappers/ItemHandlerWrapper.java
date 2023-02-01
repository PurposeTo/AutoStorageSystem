package com.chain.autostoragesystem.api.wrappers;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class ItemHandlerWrapper implements IItemHandler {
    private final IItemHandler itemHandler;

    public ItemHandlerWrapper(@Nonnull IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    // return - получилось ли переложить хотя бы часть
    // expectedAmount - количество предметов, которое желательно переложить
    public boolean moveItemStack(final int fromSlot,
                                 final int expectedAmount,
                                 @Nonnull final IItemHandler receiver) {
        ItemHandlerWrapper receiverWrap = new ItemHandlerWrapper(receiver);

        ItemStack toMoveSimulate = extractItem(fromSlot, expectedAmount, true);
        ItemStack remainingStackSimulate = receiverWrap.insertItem(toMoveSimulate, true);

        ItemStackWrapper toMoveSimulateWrap = new ItemStackWrapper(toMoveSimulate);
        if (toMoveSimulateWrap.equalsStackWithRemaining(remainingStackSimulate)) {
            return false;
        }

        int amountCanMove = toMoveSimulate.getCount() - remainingStackSimulate.getCount();
        ItemStack canToMove = extractItem(fromSlot, amountCanMove, false);

        ItemStack remainingStack = receiverWrap.insertItem(canToMove, false);

        if (!remainingStack.isEmpty()) {
            throw new IllegalStateException("remainingStack can't be not empty");
        }

        return true;
    }

    /**
     * Можно ли в инвентарь добавить предметы, хотя бы подходящего типа
     */
    public boolean isFull() {
        boolean isFull = true;

        int slots = getSlots();
        for (int slot = 0; slot < slots; slot++) {
            ItemStack stackInSlot = getStackInSlot(slot);
            ItemStackWrapper wrapper = new ItemStackWrapper(stackInSlot);

            boolean isStackFull = wrapper.isFull();
            if (!isStackFull) {
                break;
            }
        }

        return isFull;
    }

    public boolean canInsertUnstackableItem(ItemStack unstackableItem) {
        if (unstackableItem.isStackable()) {
            throw new IllegalArgumentException("This method is only for unstackable items");
        }

        int freeSlot = getFreeSlot();
        return freeSlot != -1;
    }

    /**
     * @param itemStack
     * @return Returns:
     * The remaining ItemStack that was not inserted
     * (if the entire stack is accepted, then return an empty ItemStack).
     * It may be the same as the input ItemStack if unchanged, otherwise a new ItemStack.
     * The returned ItemStack can be safely modified after.
     */
    public ItemStack insertItem(final ItemStack itemStack, boolean simulate) {
        ItemStack toInsert = itemStack.copy();

        if (toInsert.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (!toInsert.isStackable()) {
            return insertUnstackableItem(toInsert, simulate);
        }

        int slots = getSlots();
        for (int slot = 0; slot < slots; slot++) {
            ItemStack stackInSlot = getStackInSlot(slot);
            ItemStackWrapper stackInSlotWrap = new ItemStackWrapper(stackInSlot);

            if (stackInSlotWrap.canAccept(toInsert)) {
                toInsert = insertItem(slot, toInsert, simulate);
                if (toInsert.isEmpty()) {
                    break;
                }

            }
        }

        return toInsert;
    }

    /**
     * @param itemStack
     * @return Returns:
     * The remaining ItemStack that was not inserted
     * (if the entire stack is accepted, then return an empty ItemStack).
     * It may be the same as the input ItemStack if unchanged, otherwise a new ItemStack.
     * The returned ItemStack can be safely modified after.
     */
    @NotNull
    public ItemStack insertUnstackableItem(ItemStack itemStack, boolean simulate) {
        if (itemStack.isStackable()) {
            throw new IllegalArgumentException("This method is only for unstackable items");
        }

        int freeSlot = getFreeSlot();
        if (freeSlot != -1) {
            return insertItem(freeSlot, itemStack, simulate);
        } else return itemStack;
    }

    /**
     * Returns the first item stack that is empty.
     * Returns -1 if there are no empty slots
     */
    public int getFreeSlot() {
        int slots = getSlots();
        for (int slot = 0; slot < slots; slot++) {
            ItemStack stack = getStackInSlot(slot);
            if (stack.isEmpty()) {
                return slot;
            }
        }

        return -1;
    }

    /**
     * see {@link IItemHandler#getSlots()}
     **/
    @Override
    public int getSlots() {
        return this.itemHandler.getSlots();
    }

    /**
     * see {@link IItemHandler#getStackInSlot(int)}
     **/
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        return this.itemHandler.getStackInSlot(slot);
    }

    /**
     * see {@link IItemHandler#insertItem(int, ItemStack, boolean)}
     **/
    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return this.itemHandler.insertItem(slot, stack, simulate);
    }

    /**
     * see {@link IItemHandler#extractItem(int, int, boolean)}
     **/
    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.itemHandler.extractItem(slot, amount, simulate);
    }

    /**
     * see {@link IItemHandler#getSlotLimit(int)}
     */
    @Override
    public int getSlotLimit(int slot) {
        return this.itemHandler.getSlotLimit(slot);
    }

    /**
     * see {@link IItemHandler#isItemValid(int, ItemStack)}
     */
    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.itemHandler.isItemValid(slot, stack);
    }
}
