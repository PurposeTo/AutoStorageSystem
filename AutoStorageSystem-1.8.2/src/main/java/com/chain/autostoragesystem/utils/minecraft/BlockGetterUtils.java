package com.chain.autostoragesystem.utils.minecraft;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class BlockGetterUtils {

    public static Optional<BlockEntity> tryGetBlockEntity(Level level, BlockPos position) {
        return Optional.ofNullable(level.getBlockEntity(position));
    }
}
