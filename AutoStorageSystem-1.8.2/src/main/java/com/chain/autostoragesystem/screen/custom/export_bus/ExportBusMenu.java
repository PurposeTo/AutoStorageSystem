package com.chain.autostoragesystem.screen.custom.export_bus;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.entity.custom.ExportBusEntity;
import com.chain.autostoragesystem.screen.BaseMenu;
import com.chain.autostoragesystem.screen.ModMenuTypes;
import com.chain.autostoragesystem.screen.custom.ScrollableMenu;
import com.chain.autostoragesystem.screen.custom.slot.PhantomSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Set;

public class ExportBusMenu extends BaseMenu<ExportBusEntity> {

    private static final int slotsInLine = 9;
    private static final int displayedLines = 3;

    @Nonnull
    private final SimpleContainer handler;

    private final ScrollableMenu scrollableMenu;

    public ExportBusMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public ExportBusMenu(int pContainerId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.EXPORT_BUS_MENU.get(), pContainerId, inv, entity);

        this.handler = entity.getCapability(ModCapabilities.CONTAINER).orElseThrow((IllegalArgumentException::new));

        this.scrollableMenu = new ScrollableMenu(displayedLines, slotsInLine, handler);
        this.scrollableMenu.open(inv.player);

        addSlots(scrollableMenu.createMenuSlots(8, 18, PhantomSlot::new));
    }

    @Override
    protected int getTeInvSlotCount() {
        return scrollableMenu.getDisplaySize();
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
            if (!handler.hasAnyOf(Set.of(itemType))) {
                moveItemStackTo(toMove, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + getTeInvSlotCount(), false);
            }
        }

        return ItemStack.EMPTY;
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void removed(@NotNull Player playerIn) {
        this.scrollableMenu.onRemoved(playerIn);
        super.removed(playerIn);
    }

    public int getScrollIndex() {
        return scrollableMenu.getScrollIndex();
    }

    public void scrollTo(float scrollPos) {
        scrollableMenu.scrollTo(scrollPos);
    }

    public boolean canScroll() {
        return scrollableMenu.canScroll();
    }
}
