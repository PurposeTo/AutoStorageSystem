package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.api.bus.storage_bus.IStorageBus;
import com.chain.autostoragesystem.api.bus.storage_bus.StorageBus;
import com.chain.autostoragesystem.api.connection.Connection;
import com.chain.autostoragesystem.api.connection.IConnection;
import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.utils.minecraft.TickerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Шина хранения
 */
public class StorageBusEntity extends BaseBlockEntity implements TickerUtil.TickableServer {

    private final IConnection connection = new Connection(this);

    private final IStorageBus storageBus = new StorageBus(this);


    public StorageBusEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.STORAGE_BUS_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);

        registerCapability(ModCapabilities.CONNECTION, () -> connection);
        registerCapability(ModCapabilities.STORAGE_BUS, () -> storageBus);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        registerCapability(ModCapabilities.CONNECTION, () -> connection);
        registerCapability(ModCapabilities.STORAGE_BUS, () -> storageBus);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

    @Override
    public void tickServer() {

    }
}
