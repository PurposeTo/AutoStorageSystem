package com.chain.autostoragesystem.screen.slot;

import com.chain.autostoragesystem.api.wrappers.slot.SlotWithEvent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PhantomSlot extends SlotWithEvent {

	public PhantomSlot(Container inv, int slotIndex, int posX, int posY) {
		super(inv, slotIndex, posX, posY);
	}

	@Override
	public boolean mayPickup(@NotNull Player player) {
		return false;
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public boolean mayPlace(@NotNull ItemStack itemStack) {
		return true;
	}
}
