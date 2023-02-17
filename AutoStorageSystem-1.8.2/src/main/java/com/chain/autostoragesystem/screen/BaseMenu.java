package com.chain.autostoragesystem.screen;

import com.chain.autostoragesystem.utils.minecraft.MenuSlotsUtils;
import com.chain.autostoragesystem.utils.minecraft.SlotSupplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseMenu<T extends BlockEntity> extends AbstractContainerMenu {
    protected final T blockEntity;
    protected final Level level;
    protected final Inventory playerInv;

    public BaseMenu(MenuType<?> menuType, int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(menuType, pContainerId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public BaseMenu(MenuType<?> menuType, int pContainerId, Inventory inv, BlockEntity blockEntity) {
        super(menuType, pContainerId);

        this.blockEntity = (T) blockEntity;
        this.level = inv.player.level;
        this.playerInv = inv;

        addPlayerSlots(inv);
    }

    protected abstract Block getRegistryBlock();


    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    protected static final int HOTBAR_SLOT_COUNT = 9;
    protected static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    protected static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    protected static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    protected static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    protected static final int VANILLA_FIRST_SLOT_INDEX = 0;
    protected static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    protected abstract int getTeInvSlotCount(); // must be the number of slots you have!

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
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < TE_INVENTORY_FIRST_SLOT_INDEX) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + getTeInvSlotCount(), false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + getTeInvSlotCount()) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, getRegistryBlock());
    }

    protected void addPlayerSlots(Inventory playerInventory) {
        // порядок добавления важен!
        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        int slotsInLine = 9;
        int lines = 3;

        final int horizontalOffset = 8;
        final int verticalOffset = 86;

        addSlots(playerInventory, slotsInLine, lines, horizontalOffset, verticalOffset, Slot::new);
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        addSlots(playerInventory, 9, 1, 8, 144, Slot::new);
    }


    protected <T extends Slot> void addSlots(Container container,
                                             int slotsInLine,
                                             int lines,
                                             int horizontalOffset,
                                             int verticalOffset,
                                             SlotSupplier<T> slotSupplier) {
        final int firstSlotIndex = this.slots.stream()
                .filter(slot -> slot.container == container)
                .toList()
                .size();

        List<T> slots = MenuSlotsUtils.createWithPosition(container, firstSlotIndex, slotsInLine, lines, horizontalOffset, verticalOffset, slotSupplier);
        addSlots(slots);
    }

    protected <T extends Slot> void addSlots(@NotNull List<T> slots) {
        slots.forEach(this::addSlot);
    }
}

