package com.chain.autostoragesystem.block.custom.base;

import com.chain.autostoragesystem.utils.minecraft.TickerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractBlockWithEntity extends BaseEntityBlock {

    protected AbstractBlockWithEntity(Properties properties) {
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
        return TickerUtil.createTickers(level);
    }

    protected InteractionResult openMenu(@NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        boolean clientSide = level.isClientSide();

        if (!clientSide) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity == null) {
                throw new IllegalStateException("Block entity is missing!");
            }

            if (entity instanceof MenuProvider menuProvider) {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                NetworkHooks.openGui(serverPlayer, menuProvider, pos);
            } else {
                throw new IllegalStateException("Menu provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(clientSide);
    }
}
