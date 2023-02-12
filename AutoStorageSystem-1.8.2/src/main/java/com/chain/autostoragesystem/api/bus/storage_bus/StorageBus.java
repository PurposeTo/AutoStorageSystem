package com.chain.autostoragesystem.api.bus.storage_bus;

import com.chain.autostoragesystem.api.connection.IConnection;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class StorageBus implements IStorageBus {

    @Nonnull
    private final BlockEntity blockEntity;

    @Nonnull
    private final IConnection connection;

    public StorageBus(@Nonnull BlockEntity blockEntity, @Nonnull IConnection connection) {
        this.blockEntity = blockEntity;
        this.connection = connection;
    }

    @NotNull
    @Override
    public List<IItemHandlerWrapper> getStorageItemHandlers() {
        return connection.getNeighboursItemHandlers();
    }
}
