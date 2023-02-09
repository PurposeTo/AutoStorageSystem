package com.chain.autostoragesystem.api.connection;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

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
        return blockEntity.getLevel();
    }
}
