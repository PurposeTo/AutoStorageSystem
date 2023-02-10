package com.chain.autostoragesystem.api.bus.storage_bus;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Objects;

public class StorageBus implements IStorageBus {

    @Nonnull
    private final BlockEntity blockEntity;

    public StorageBus(@Nonnull BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    public @NotNull BlockPos getSourcePos() {
        return blockEntity.getBlockPos();
    }

    @Override
    public @NotNull Level getLevel() {
        return Objects.requireNonNull(blockEntity.getLevel());
    }
}
