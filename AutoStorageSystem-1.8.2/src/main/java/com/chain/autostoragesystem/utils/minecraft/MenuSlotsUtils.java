package com.chain.autostoragesystem.utils.minecraft;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MenuSlotsUtils {
    private final static int slotHeight = 16;
    private final static int slotWidth = 16;
    private final static int horizontalSpacing = 2;
    private final static int verticalSpacing = 2;

    public static <T extends Slot> List<T> createWithPosition(@Nonnull Container container,
                                                              final int firstSlotIndex,
                                                              final int slotsInLine,
                                                              final int lines,
                                                              final int horizontalOffset,
                                                              final int verticalOffset,
                                                              @Nonnull SlotSupplier<T> slotSupplier) {
        assert slotsInLine * lines >= container.getContainerSize();
        List<T> slots = new ArrayList<>();

        for (int i = 0; i < lines; ++i) {
            for (int l = 0; l < slotsInLine; ++l) {
                int posX = horizontalOffset + (l * (slotWidth + horizontalSpacing));
                int posY = verticalOffset + (i * (slotHeight + verticalSpacing));
                int slotIndex = l + i * slotsInLine + firstSlotIndex;
                T slot = slotSupplier.create(container, slotIndex, posX, posY);
                slots.add(slot);
            }
        }
        return slots;
    }
}
