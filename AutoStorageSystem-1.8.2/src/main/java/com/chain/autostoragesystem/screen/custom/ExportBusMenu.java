package com.chain.autostoragesystem.screen.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.block.ModBlocks;
import com.chain.autostoragesystem.entity.custom.ExportBusEntity;
import com.chain.autostoragesystem.screen.BaseMenu;
import com.chain.autostoragesystem.screen.ModMenuTypes;
import com.chain.autostoragesystem.screen.custom.slot.PhantomSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Set;

public class ExportBusMenu extends BaseMenu<ExportBusEntity> {

    @Nonnull
    private final Container filters;

    public ExportBusMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public ExportBusMenu(int pContainerId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.EXPORT_BUS_MENU.get(), pContainerId, inv, entity);

        this.filters = entity.getCapability(ModCapabilities.CONTAINER).orElseThrow((IllegalArgumentException::new));
        filters.startOpen(inv.player);

        addSlots(filters, 9, 3, 8, 18, PhantomSlot::new);
    }

    @Override
    protected Block getRegistryBlock() {
        return ModBlocks.EXPORT_BUS_BLOCK.get();
    }

    @Override
    protected int getTeInvSlotCount() {
        return filters.getContainerSize();
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        Item itemType = sourceStack.getItem();

        ItemStack toMove = new ItemStack(itemType, 1);

        // Check if the slot clicked is one of the vanilla container slots
        if (index < TE_INVENTORY_FIRST_SLOT_INDEX) {

            // If TE inventory has not this item
            if (!filters.hasAnyOf(Set.of(itemType))) {
                moveItemStackTo(toMove, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + getTeInvSlotCount(), false);
            }
        }

        return ItemStack.EMPTY;
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.filters.stopOpen(playerIn);
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType click, Player player) {
        Slot slot = slotId > -1 && slotId < slots.size() ? slots.get(slotId) : null;
        if (slot instanceof PhantomSlot) {
            ItemStack s = getCarried().copy();
            if (!s.isEmpty()) s.setCount(1);
            slot.set(s);
            return;
        }
        super.clicked(slotId, dragType, click, player);
    }
}
