package com.chain.autostoragesystem.utils.minecraft;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public interface SlotSupplier<T extends Slot> {
    T create(Container pContainer, int pIndex, int pX, int pY);

}
