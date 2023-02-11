package com.chain.autostoragesystem.block.custom.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class BlockWithEntity extends AbstractBlockWithEntity {
    private final BlockEntitySupplier blockEntitySupplier;

    public BlockWithEntity(Properties properties, @Nonnull BlockEntitySupplier supplier) {
        super(properties);
        this.blockEntitySupplier = supplier;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return blockEntitySupplier.create(pPos, pState);
    }

    public interface BlockEntitySupplier {
        BlockEntity create(BlockPos pos, BlockState state);
    }
}
