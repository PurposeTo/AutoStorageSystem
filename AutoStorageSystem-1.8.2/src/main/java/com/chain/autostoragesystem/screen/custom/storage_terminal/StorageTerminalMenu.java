package com.chain.autostoragesystem.screen.custom.storage_terminal;

import com.chain.autostoragesystem.entity.custom.ExportBusEntity;
import com.chain.autostoragesystem.screen.BaseMenu;
import com.chain.autostoragesystem.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

public class StorageTerminalMenu extends BaseMenu<ExportBusEntity> {


    public StorageTerminalMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public StorageTerminalMenu(int pContainerId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.STORAGE_TERMINAL_MENU.get(), pContainerId, inv, entity);

    }

    @Override
    protected int getTeInvSlotCount() {
        return 0;
    }
}
