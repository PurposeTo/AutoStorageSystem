package com.chain.autostoragesystem.screen.custom.export_bus;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.block.ModBlocks;
import com.chain.autostoragesystem.entity.custom.ExportBusEntity;
import com.chain.autostoragesystem.screen.BaseMenu;
import com.chain.autostoragesystem.screen.ModMenuTypes;
import com.chain.autostoragesystem.screen.custom.slot.PhantomSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
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

    @NotNull
    private final Container displayedContainer = new SimpleContainer(3 * 9);

    private final int slotsInLine = 9;
    private final int displayedLines = 3;

    public ExportBusMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public ExportBusMenu(int pContainerId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.EXPORT_BUS_MENU.get(), pContainerId, inv, entity);

        Container container = entity.getCapability(ModCapabilities.CONTAINER).orElseThrow((IllegalArgumentException::new));

        this.filters = container;

        displayedContainer.startOpen(inv.player);

        addSlots(displayedContainer, 9, 3, 8, 18, PhantomSlot::new);
    }

    @Override
    protected Block getRegistryBlock() {
        return ModBlocks.EXPORT_BUS_BLOCK.get();
    }

    @Override
    protected int getTeInvSlotCount() {
        return displayedContainer.getContainerSize();
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
        this.displayedContainer.stopOpen(playerIn);
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

    /**
     * Updates the gui slot's ItemStacks based on scroll position.
     */
    public void scrollTo(float pPos) {
        int slotsNeedDisplayCount = filters.getContainerSize(); // сколько всего слотов может отобразить

        int i = (slotsNeedDisplayCount + 9 - 1) / 9 - displayedLines;
        int j = (int) ((double) (pPos * (float) i) + 0.5D);
        if (j < 0) {
            j = 0;
        }

        for (int k = 0; k < displayedLines; ++k) {
            for (int l = 0; l < 9; ++l) {
                int i1 = l + (k + j) * 9;

                int slotIndex = l + k * 9;
                if (i1 >= 0 && i1 < slotsNeedDisplayCount) {
                    ItemStack stackToDisplay = filters.getItem(i1);
                    displayedContainer.setItem(slotIndex, stackToDisplay);
                } else {
                    displayedContainer.setItem(slotIndex, ItemStack.EMPTY);
                }
            }
        }

    }

    /**
     * Можно скролить, если не все предметы отображаются
     */
    public boolean canScroll() {
        return this.filters.getContainerSize() > displayedContainer.getContainerSize();
    }

    //todo что это за число?
    public int getFoo() {
        int slotsCount = filters.getContainerSize(); // сколько всего слотов
        return (slotsCount + 9 - 1) / 9 - displayedLines;
    }
}
