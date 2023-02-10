package com.chain.autostoragesystem.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class DebugItem extends Item {

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


            }
        }

        return super.useOn(pContext);
    }

}
