package com.chain.autostoragesystem.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

import static com.chain.autostoragesystem.utils.minecraft.EntitiesUtils.*;

public class NeighborsApi {

    public static <T> List<T> get(Level level, BlockPos sourcePos, Capability<T> cap) {
        List<BlockPos> neighborsPos = getNeighborsPos(sourcePos);
        List<BlockEntity> neighborsEntity = mapToEntities(level, neighborsPos);
        return mapToCapability(neighborsEntity, cap);
    }


    public static List<IItemHandler> getItemHandlers(Level level, BlockPos sourcePos) {
        //The client doesn't know what is in a chest unless that chest is opened.
        //need to invoke on server
        if (level.isClientSide) {
            throw new IllegalStateException("The client doesn't know what is in a chest unless that chest is opened. Must have to invoke this only on server side.");
        }

        return get(level, sourcePos, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    }


}
