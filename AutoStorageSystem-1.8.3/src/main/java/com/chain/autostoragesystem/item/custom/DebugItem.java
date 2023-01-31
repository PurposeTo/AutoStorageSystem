package com.chain.autostoragesystem.item.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.block.custom.SystemController;
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
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DebugItem extends Item {

    private SystemController systemController = null;

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

                BlockGetterUtils.tryGetBlockEntity(pContext.getLevel(), position)
                        .ifPresent(blockEntity -> {
                            blockEntity.getCapability(ModCapabilities.IMPORT_BUS_CAPABILITY)
                                    .ifPresent((importBus) -> {
                                        if (this.systemController != null) {
                                            this.systemController.addImportBus(player, importBus);
                                        }
                                    });
                        });
            }
        }

        return super.useOn(pContext);
    }

    public void trySetController(@NotNull Level level,
                                 @NotNull Player player,
                                 @NotNull BlockPos position) {

        Block block = level.getBlockState(position).getBlock();

        if (block instanceof SystemController) {
            this.systemController = (SystemController) block;

            String log = "Setting done: " + NamesUtil.getBlockNameAndCoordinatesLog(position, block);
            ChatUtil.playerSendMessage(player, log);
        }
    }
}
