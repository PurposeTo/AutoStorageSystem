package com.chain.autostoragesystem.utils.minecraft;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Block;

public class NamesUtil {


    public static String getBlockName(Block block) {
        return new TranslatableComponent(block.getName().getString()).toString();
    }

    public static String getBlockNameAndCoordinatesLog(BlockPos blockPos, Block block) {
        String blockName = NamesUtil.getBlockName(block);
        return String.format("Found %s at (%s, %s, %s)", blockName, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
}
