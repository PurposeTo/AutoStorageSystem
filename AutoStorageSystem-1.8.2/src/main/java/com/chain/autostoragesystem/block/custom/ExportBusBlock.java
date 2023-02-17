package com.chain.autostoragesystem.block.custom;

import com.chain.autostoragesystem.block.custom.base.AbstractBlockWithEntity;
import com.chain.autostoragesystem.entity.custom.ExportBusEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Шина экспорта предметов ИЗ системы
 */
public class ExportBusBlock extends AbstractBlockWithEntity {

    public ExportBusBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new ExportBusEntity(pPos, pState);
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState pState,
                                 @NotNull Level pLevel,
                                 @NotNull BlockPos pPos,
                                 @NotNull Player pPlayer,
                                 @NotNull InteractionHand pHand,
                                 @NotNull BlockHitResult pHit) {
        return openMenu(pLevel, pPos, pPlayer);
    }
}
