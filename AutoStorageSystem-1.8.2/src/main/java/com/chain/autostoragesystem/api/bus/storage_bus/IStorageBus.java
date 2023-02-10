package com.chain.autostoragesystem.api.bus.storage_bus;

import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.item_handler.ItemHandlerWrapper;
import com.chain.autostoragesystem.utils.minecraft.Levels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface IStorageBus {

    /**
     * Получить IItemHandler-ы, которые находятся вокруг указанной позиции
     * Исключая диагонали
     */
    @Nonnull
    default List<IItemHandlerWrapper> getNeighboursItemHandlers() {
        //The client doesn't know what is in a chest unless that chest is opened.
        //need to invoke on server
        Level level = getLevel();
        Levels.requireServerSide(level);

        List<IItemHandler> list = new ArrayList<>();

        Arrays.stream(Direction.values())
                .forEach(direction -> {
                    BlockPos position = getSourcePos().relative(direction);
                    BlockEntity blockEntity = level.getBlockEntity(position);

                    if (blockEntity != null) {
                        LazyOptional<IItemHandler> lazyOptional = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite());
                        lazyOptional.ifPresent(list::add);
                    }
                });

        return list
                .stream()
                .map(ItemHandlerWrapper::new)
                .collect(Collectors.toList());
    }

    @Nonnull
    BlockPos getSourcePos();

    @Nonnull
    Level getLevel();
}
