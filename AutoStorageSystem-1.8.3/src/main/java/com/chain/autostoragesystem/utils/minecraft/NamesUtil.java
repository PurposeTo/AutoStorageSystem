package com.chain.autostoragesystem.utils.minecraft;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NamesUtil {

    public static String getBlockEntityName(BlockEntity blockEntity) {
        return getBlockName(blockEntity.getBlockState().getBlock());
    }

    public static String getBlockName(Block block) {
        return new TranslatableComponent(block.getName().getString()).getKey();
    }

    public static String getBlockNameAndCoordinatesLog(BlockPos blockPos, Block block) {
        String blockName = NamesUtil.getBlockName(block);
        return String.format("%s at (%s, %s, %s)", blockName, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static String getBlockEntityNameAndCoordinatesLog(BlockPos blockPos, BlockEntity blockEntity) {
        return getBlockNameAndCoordinatesLog(blockPos, blockEntity.getBlockState().getBlock());
    }
}
