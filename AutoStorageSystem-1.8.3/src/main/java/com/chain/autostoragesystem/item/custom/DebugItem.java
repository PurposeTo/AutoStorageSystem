package com.chain.autostoragesystem.item.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.ModMain;
import com.chain.autostoragesystem.block.custom.SystemController;
import com.chain.autostoragesystem.utils.minecraft.BlockGetterUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class DebugItem extends Item {

    private SystemController systemController = null;

    public DebugItem(CreativeModeTab tab) {
        super(new Item.Properties().tab(Objects.requireNonNull(tab)));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide()) {
            BlockPos positionClicked = pContext.getClickedPos();
            Player player = pContext.getPlayer();
            AtomicBoolean foundBlock = new AtomicBoolean(false);

            for (int i = 0; i <= positionClicked.getY() + 64; i++) {
                BlockPos position = positionClicked.below(i);

                Block block = pContext.getLevel().getBlockState(position).getBlock();

                // save system controller
                if (block != null) {
                    if (block instanceof SystemController) {
                        this.systemController = (SystemController) block;
                    }
                }

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
}
