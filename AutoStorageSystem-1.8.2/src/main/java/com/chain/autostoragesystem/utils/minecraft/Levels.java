package com.chain.autostoragesystem.utils.minecraft;

import net.minecraft.world.level.Level;

public class Levels {
    public static void requireClientSide(Level level) {
        if (!level.isClientSide) {
            throw new IllegalStateException("This is a server's side. Need to invoke on client");
        }
    }

    public static void requireServerSide(Level level) {
        if (level.isClientSide) {
            throw new IllegalStateException("This is a client's side. Need to invoke on server");
        }
    }
}
