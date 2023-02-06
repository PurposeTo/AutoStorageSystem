package com.chain.autostoragesystem.block.custom;

import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.entity.custom.ExportBusEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Шина экспорта предметов ИЗ системы
 */
public class ExportBusBlock extends BaseEntityBlock {

    public ExportBusBlock(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(
                pBlockEntityType,
                ModBlockEntities.EXPORT_BUS_BLOCK_ENTITY.get(),
                level.isClientSide() ? ExportBusEntity::clientTick : ExportBusEntity::serverTick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ExportBusEntity(pPos, pState);
    }
}
