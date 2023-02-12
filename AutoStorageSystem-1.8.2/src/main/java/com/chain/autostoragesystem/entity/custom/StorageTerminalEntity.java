package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.entity.custom.base.BaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class StorageTerminalEntity extends BaseBlockEntity {
    public StorageTerminalEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.STORAGE_TERMINAL_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }


}
