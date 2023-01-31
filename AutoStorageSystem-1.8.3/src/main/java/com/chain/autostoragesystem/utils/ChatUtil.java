package com.chain.autostoragesystem.utils;

import com.chain.autostoragesystem.utils.common.StringUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class ChatUtil {

    public static void playerSendMessage(Player player, String message) {
        Objects.requireNonNull(player);
        StringUtils.requiredNonBlank(message);

        player.sendMessage(new TextComponent(message), player.getUUID());
    }
}
