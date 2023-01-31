package com.chain.autostoragesystem.item.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.api.IImportBus;
import com.chain.autostoragesystem.entity.custom.SystemControllerEntity;
import com.chain.autostoragesystem.utils.ChatUtil;
import com.chain.autostoragesystem.utils.minecraft.BlockGetterUtils;
import com.chain.autostoragesystem.utils.minecraft.NamesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DebugItem extends Item {

    private LazyOptional<SystemControllerEntity> systemControllerOp = LazyOptional.empty();

    public DebugItem(CreativeModeTab tab) {
        super(new Item.Properties().tab(Objects.requireNonNull(tab)));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();

        if (!level.isClientSide()) {
            BlockPos positionClicked = pContext.getClickedPos();
            Player player = pContext.getPlayer();

            for (int i = 0; i <= positionClicked.getY() + 64; i++) {
                BlockPos position = positionClicked.below(i);

                trySetController(level, player, position);

                BlockGetterUtils.tryGetBlockEntity(level, position)
                        .ifPresent(blockEntity -> {
                            LazyOptional<IImportBus> importBusLazyOptional = blockEntity.getCapability(ModCapabilities.IMPORT_BUS_CAPABILITY);

                            systemControllerOp.ifPresent(systemController -> {
                                systemController.addImportBus(player, importBusLazyOptional);
                            });
                        });
            }
        }

        return super.useOn(pContext);
    }

    public void trySetController(@NotNull Level level,
                                 @NotNull Player player,
                                 @NotNull BlockPos position) {

        BlockGetterUtils.tryGetBlockEntity(level, position)
                .ifPresent((blockEntity -> {
                    if (blockEntity instanceof SystemControllerEntity systemController) {
                        systemController.player = player;

                        this.systemControllerOp = LazyOptional.of(() -> systemController);

                        String log = "Setting done: " + NamesUtil.getBlockEntityNameAndCoordinatesLog(position, blockEntity);
                        ChatUtil.playerSendMessage(player, log);
                    }
                }));
    }
}
