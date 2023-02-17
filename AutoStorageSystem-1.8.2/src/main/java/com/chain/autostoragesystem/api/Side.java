package com.chain.autostoragesystem.api;

import net.minecraft.world.level.Level;

public enum Side {
    CLIENT,
    SERVER;

    public static Side get(Level level) {
        return level.isClientSide() ?
                CLIENT :
                SERVER;
    }
}
