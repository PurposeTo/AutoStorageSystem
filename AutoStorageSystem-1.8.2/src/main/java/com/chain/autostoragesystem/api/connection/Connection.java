package com.chain.autostoragesystem.api.connection;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.api.bus.storage_bus.IStorageBus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Connection implements IConnection {

    @Nonnull
    private final BlockEntity blockEntity;

    public Connection(@Nonnull BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return blockEntity.getCapability(cap, side);
    }

    @Override
    public @NotNull BlockPos getSourcePos() {
        return blockEntity.getBlockPos();
    }

    @Override
    public @NotNull Level getLevel() {
        return Objects.requireNonNull(blockEntity.getLevel());
    }

    public List<IStorageBus> getStorageBusses() {
        return getAllConnections()
                .stream()
                .flatMap(connection -> connection.getCapability(ModCapabilities.STORAGE_BUS).resolve().stream())
                .collect(Collectors.toList());
    }
}
