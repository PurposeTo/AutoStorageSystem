package com.chain.autostoragesystem.utils.minecraft;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import org.jetbrains.annotations.Nullable;

public class TickerUtil {

    @Nullable
    public static <T extends BlockEntity> BlockEntityTicker<T> createTickerServer(Level world) {
        if (world.isClientSide) return null;
        else return (a, b, c, tile) -> ((TickableServer) tile).tickServer();
    }

    @Nullable
    public static <T extends BlockEntity> BlockEntityTicker<T> createTickerClient(Level world) {
        if (world.isClientSide) return (a, b, c, tile) -> ((TickableClient) tile).tickClient();
        else return null;
    }

    public static <T extends BlockEntity> BlockEntityTicker<T> createTickers(Level world) {
        if (world.isClientSide) {
            return (a, b, c, tile) -> {
                if (tile instanceof TickableClient tickableClient) {
                    tickableClient.tickClient();
                }
            };
        } else {
            return (a, b, c, tile) -> {
                if (tile instanceof TickableServer tickableServer) {
                    tickableServer.tickServer();
                }
            };
        }
    }

    public interface TickableClient {
        void tickClient();
    }

    public interface TickableServer {
        void tickServer();
    }
}
