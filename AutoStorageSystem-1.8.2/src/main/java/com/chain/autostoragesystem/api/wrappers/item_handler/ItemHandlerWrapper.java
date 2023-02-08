package com.chain.autostoragesystem.api.wrappers.item_handler;

import com.chain.autostoragesystem.api.wrappers.ItemStackWrapper;
import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.StackInSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemHandlerWrapper implements IItemHandlerWrapper {
    private final IItemHandler itemHandler;

    public ItemHandlerWrapper(@Nonnull IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public @NotNull List<StackInSlot> getStacks() {
        int slots = this.getSlots();
        List<StackInSlot> result = new ArrayList<>();
        for (int slot = 0; slot < slots; slot++) {
            StackInSlot inventoryStack = new StackInSlot(this, slot);
            result.add(inventoryStack);
        }
        return result;
    }

    @Override
    public @NotNull List<IStackInSlot> getNotEmptyStacks() {
        return getStacks().stream()
                .filter(IStackInSlot::notEmpty)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull List<IStackInSlot> getEmptyStacks() {
        return getStacks().stream()
                .filter(IStackInSlot::isEmpty)
                .collect(Collectors.toList());
    }

    @Override
    public ItemStack moveItemStack(final StackInSlot toMove, final int toMoveCount, final IItemsReceiver itemsReceiver) {
        ItemStack toMoveStack = toMove.extractItemStack(toMoveCount, false);
        ItemStack remainingStack = itemsReceiver.insertItem(toMoveStack, false);

        if (!remainingStack.isEmpty()) {
            toMove.insertItem(remainingStack, false);
        }

        return remainingStack;
    }

    /**
     * @param itemStack
     * @return Returns:
     * The remaining ItemStack that was not inserted
     * (if the entire stack is accepted, then return an empty ItemStack).
     * It may be the same as the input ItemStack if unchanged, otherwise a new ItemStack.
     * The returned ItemStack can be safely modified after.
     */
    @Override
    public @NotNull ItemStack insertItem(final @NotNull ItemStack itemStack, boolean simulate) {
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
    private ItemStack insertUnstackableItem(ItemStack itemStack, boolean simulate) {
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
    private int getFreeSlot() {
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
    private int getSlots() {
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
    public ItemStack insertItem(int slot, @NotNull ItemStack itemStack, boolean simulate) {
        return this.itemHandler.insertItem(slot, itemStack, simulate);
    }

    /**
     * see {@link IItemHandler#extractItem(int, int, boolean)}
     **/
    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.itemHandler.extractItem(slot, amount, simulate);
    }
}
