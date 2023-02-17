package com.chain.autostoragesystem.api.wrappers.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class SlotWithEvent extends Slot {
    List<ISlotListener> listeners = new ArrayList<>();

    public SlotWithEvent(Container pContainer, int pIndex, int pX, int pY) {
        super(pContainer, pIndex, pX, pY);
    }

    public void addListener(ISlotListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(ISlotListener listener) {
        listeners.remove(listener);
    }

    public void clearAllListeners() {
        listeners.clear();
    }

    @Override
    public void setChanged() {
        listeners.forEach(listener -> listener.onChanged(getSlotIndex(), getItem()));
    }
}
