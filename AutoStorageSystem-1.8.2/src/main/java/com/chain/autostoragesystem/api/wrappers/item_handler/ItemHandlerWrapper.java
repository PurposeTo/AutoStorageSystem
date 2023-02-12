package com.chain.autostoragesystem.api.wrappers.item_handler;

import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.StackInSlot;
import lombok.Getter;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemHandlerWrapper implements IItemHandlerWrapper {

    @Getter
    private final IItemHandler itemHandler;

    public ItemHandlerWrapper(@Nonnull IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @NotNull
    @Override
    public List<IStackInSlot> getStacks() {
        int slots = this.getSlots();
        List<IStackInSlot> result = new ArrayList<>();
        for (int slot = 0; slot < slots; slot++) {
            StackInSlot stack = new StackInSlot(itemHandler, slot);
            result.add(stack);
        }
        return result;
    }

    @NotNull
    @Override
    public List<IStackInSlot> getNotEmptyStacks() {
        return getStacks().stream()
                .filter(IStackInSlot::notEmpty)
                .collect(Collectors.toList());
    }

    @NotNull
    @Override
    public List<IStackInSlot> getEmptyStacks() {
        return getStacks().stream()
                .filter(IStackInSlot::isEmpty)
                .collect(Collectors.toList());
    }

    @Override
    public ItemStack moveItemStack(final StackInSlot toMove, final int toMoveCount, final IItemsReceiver itemsReceiver) {
        return toMove.moveItemStack(toMoveCount, itemsReceiver);
    }

    /**
     * @return Returns:
     * The remaining ItemStack that was not inserted
     * (if the entire stack is accepted, then return an empty ItemStack).
     * It may be the same as the input ItemStack if unchanged, otherwise a new ItemStack.
     * The returned ItemStack can be safely modified after.
     */
    @NotNull
    @Override
    public ItemStack insertItem(final @NotNull ItemStack itemStack, boolean simulate) {
        return ItemHandlerHelper.insertItemStacked(this.itemHandler, itemStack, simulate);
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

    //todo а если понадобиться сравнивать с другими реализациями этого интерфейса?
    @Override
    public boolean same(@NotNull IItemHandlerWrapper itemHandler) {
        return this.itemHandler == itemHandler.unwrap();
    }

    @Override
    public IItemHandler unwrap() {
        return itemHandler;
    }
}
