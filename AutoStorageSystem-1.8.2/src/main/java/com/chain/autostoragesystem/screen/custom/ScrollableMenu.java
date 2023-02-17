package com.chain.autostoragesystem.screen.custom;

import com.chain.autostoragesystem.api.wrappers.container.ContainerWrapper;
import com.chain.autostoragesystem.api.wrappers.slot.SlotWithEvent;
import com.chain.autostoragesystem.utils.minecraft.MenuSlotsUtils;
import com.chain.autostoragesystem.utils.minecraft.SlotSupplier;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class ScrollableMenu {

    private final int slotsInLine = 9;
    private final int lines;
    @NotNull
    private final SimpleContainer display;

    @NotNull
    private final SimpleContainer handler;

    private int hiddenSlots = 0; // индекс первого отображаемого слота

    private List<? extends SlotWithEvent> slots;

    private final ContainerListener handlerListener = new ContainerListener() {

        /**
         * Called by InventoryBasic.onInventoryChanged() on an array that is never filled.
         *
         */
        @Override
        public void containerChanged(@NotNull Container pInvBasic) {
            ContainerWrapper wrapper = new ContainerWrapper(pInvBasic);
            List<ItemStack> items = wrapper.getStacks();

            for (int i = 0; i < items.size(); i++) {
                ItemStack stack = items.get(i);

                int displaySlotIndex = i + hiddenSlots;

                if (displaySlotIndex < display.getContainerSize()) {
                    display.setItem(displaySlotIndex, stack);
                }
            }
        }
    };

    public ScrollableMenu(int lines, @NotNull SimpleContainer handler) {
        this.lines = lines;
        this.display = new SimpleContainer(lines * slotsInLine);
        this.handler = handler;

        this.handler.addListener(handlerListener);
    }

    public void open(Player player) {
        display.startOpen(player);
        scrollTo(0.0f);
    }


    public void onRemoved(Player player) {
        display.stopOpen(player);
        slots.forEach(SlotWithEvent::clearAllListeners);
    }

    /**
     * Updates the gui slot's ItemStacks based on scroll position.
     *
     * @param scrollPos scroll value from 0 to 1
     */
    public void scrollTo(float scrollPos) {
        final int slotsInLine = 9;

        int slotsCount = handler.getContainerSize(); // сколько всего слотов

        int hiddenLines = (int) (((slotsCount / slotsInLine) - lines) * scrollPos);
        if (hiddenLines < 0) {
            hiddenLines = 0;
        }

        reDrawDisplay(hiddenLines);
    }

    public void reDrawDisplay(int hiddenLines) {
        this.hiddenSlots = hiddenLines * slotsInLine;
        int slotsCount = handler.getContainerSize(); // сколько всего слотов

        display.clearContent();
        // для каждой строки
        for (int line = 0; line < lines; ++line) {

            // для каждого слота в линии
            for (int indexInLine = 0; indexInLine < slotsInLine; ++indexInLine) {
                int lastSlots = line * slotsInLine;

                int slotIndex = lastSlots + indexInLine;
                int handlerSlotIndex = hiddenSlots + slotIndex;

                if (handlerSlotIndex >= 0 && handlerSlotIndex < slotsCount) {


                    ItemStack stackToDisplay = handler.getItem(handlerSlotIndex);
                    display.setItem(slotIndex, stackToDisplay);
                } else {
                    display.setItem(slotIndex, ItemStack.EMPTY);
                }
            }
        }
    }

    /**
     * Можно скролить, если не все предметы отображаются
     */
    public boolean canScroll() {
        return this.handler.getContainerSize() > display.getContainerSize();
    }

    public int getScrollIndex() {
        int slotsCount = handler.getContainerSize(); // сколько всего слотов
        return (slotsCount + slotsInLine - 1) / slotsInLine - lines;
    }

    public int getDisplaySize() {
        return display.getContainerSize();
    }

    public <T extends SlotWithEvent> List<T> createMenuSlots(final int horizontalOffset,
                                                             final int verticalOffset,
                                                             @Nonnull SlotSupplier<T> slotSupplier) {
        List<T> slots = MenuSlotsUtils.createWithPosition(display, 0, slotsInLine, lines, horizontalOffset, verticalOffset, slotSupplier);
        slots.forEach(slot -> slot.addListener(this::updateHandlerSlot));
        this.slots = slots;
        return slots;
    }

    private void updateHandlerSlot(int slotIndex, ItemStack itemStack) {
        // need to update ONLY listener, without display
        this.handler.removeListener(handlerListener);
        this.handler.setItem(hiddenSlots + slotIndex, itemStack);
        this.handler.addListener(handlerListener);
    }
}
