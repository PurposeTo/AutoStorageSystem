package com.chain.autostoragesystem.api.connection;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.item_handler.ItemHandlerWrapper;
import com.chain.autostoragesystem.utils.minecraft.Levels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static com.chain.autostoragesystem.utils.minecraft.EntitiesUtils.mapToCapability;
import static com.chain.autostoragesystem.utils.minecraft.EntitiesUtils.mapToEntities;

/**
 * Описывает блок, который позволяет быть проводником системы
 */
public interface IConnection extends ICapabilityProvider {

    /**
     * Получить все подключения, за исключением текущего
     */
    @Nonnull
    default Set<IConnection> getAllConnections() {
        Set<IConnection> connections = new HashSet<>();

        Set<IConnection> newConnections = new HashSet<>();
        newConnections.add(this);

        while (!newConnections.isEmpty()) {
            for (IConnection connection : newConnections) {
                connections.add(connection);
                newConnections.addAll(connection.getNeighborConnections());
                newConnections.removeAll(connections);
            }
        }

        connections.remove(this);

        return connections;
    }

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
    default List<BlockPos> getNeighborsPos() {
        BlockPos sourcePos = getSourcePos();
        return List.of(sourcePos.north(), sourcePos.east(), sourcePos.south(), sourcePos.west(), sourcePos.above(), sourcePos.below());
    }

    /**
     * Получить подключения к этому блоку
     */
    @Nonnull
    default Set<IConnection> getNeighborConnections() {
        return new HashSet<>(getNeighboursCapabilities(ModCapabilities.CONNECTION_CAPABILITY));
    }

    @Nonnull
    default <T> List<T> getNeighboursCapabilities(Capability<T> cap) {
        List<BlockPos> neighborsPos = getNeighborsPos();
        List<BlockEntity> neighborsEntity = mapToEntities(getLevel(), neighborsPos);
        return mapToCapability(neighborsEntity, cap);
    }

    @Nonnull
    BlockPos getSourcePos();

    @Nonnull
    Level getLevel();
}
