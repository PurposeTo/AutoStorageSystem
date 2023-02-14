package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.entity.custom.base.BaseBlockEntity;
import com.chain.autostoragesystem.screen.custom.storage_terminal.StorageTerminalMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StorageTerminalEntity extends BaseBlockEntity implements MenuProvider {
    public StorageTerminalEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.STORAGE_TERMINAL_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    @NotNull
    @Override
    public Component getDisplayName() {
        return new TextComponent("S. terminal");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new StorageTerminalMenu(pContainerId, pInventory, this);
    }
}
