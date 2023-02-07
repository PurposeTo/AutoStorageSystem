package com.chain.autostoragesystem.screen.custom;

import com.chain.autostoragesystem.block.ModBlocks;
import com.chain.autostoragesystem.entity.custom.ExportBusEntity;
import com.chain.autostoragesystem.screen.BaseMenu;
import com.chain.autostoragesystem.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ExportBusMenu extends BaseMenu<ExportBusEntity> {


    public ExportBusMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public ExportBusMenu(int pContainerId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.EXPORT_BUS_MENU.get(), pContainerId, inv, entity);

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    @Override
    protected Block getRegistryBlock() {
        return ModBlocks.EXPORT_BUS_BLOCK.get();
    }

    @Override
    protected int getMenuSlotCount() {
        return 27;
    }

}
