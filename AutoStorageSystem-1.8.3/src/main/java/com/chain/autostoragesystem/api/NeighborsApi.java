package com.chain.autostoragesystem.api;

import com.chain.autostoragesystem.utils.minecraft.Levels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import static com.chain.autostoragesystem.utils.minecraft.EntitiesUtils.*;

public class NeighborsApi {

    public static <T> List<T> get(Level level, BlockPos sourcePos, Capability<T> cap) {
        List<BlockPos> neighborsPos = getNeighborsPos(sourcePos);
        List<BlockEntity> neighborsEntity = mapToEntities(level, neighborsPos);
        return mapToCapability(neighborsEntity, cap);
    }


    /**
     * Получить IItemHandler-ы, которые находятся вокруг указанной позиции
     * Исключая диагонали
     */
    public static LinkedHashSet<IItemHandler> getItemHandlers(Level level, BlockPos sourcePos) {
        //The client doesn't know what is in a chest unless that chest is opened.
        //need to invoke on server
        Levels.requireServerSide(level);

        LinkedHashSet<IItemHandler> orderedSet = new LinkedHashSet<>();

        Arrays.stream(Direction.values())
                .forEach(direction -> {
                    BlockPos position = sourcePos.relative(direction);
                    BlockEntity blockEntity = level.getBlockEntity(position);

                    if (blockEntity != null) {
                        LazyOptional<IItemHandler> lazyOptional = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite());

                        lazyOptional.ifPresent(orderedSet::add);
                    }
                });

        return orderedSet;
    }


}
