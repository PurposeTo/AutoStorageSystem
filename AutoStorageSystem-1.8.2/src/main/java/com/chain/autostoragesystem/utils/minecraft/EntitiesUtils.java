package com.chain.autostoragesystem.utils.minecraft;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EntitiesUtils {

    public static List<BlockEntity> mapToEntities(Level level, List<BlockPos> positions) {
        return positions.stream()
                .map(level::getBlockEntity)
                .filter(Objects::nonNull)
                .toList();
    }

    public static <T> List<T> mapToCapability(List<BlockEntity> blockEntities, Capability<T> cap) {
        return blockEntities.stream()
                .map(entity -> entity.getCapability(cap))
                .map(LazyOptional::resolve)
                .flatMap(Optional::stream)
                .toList();
    }

}
