package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.api.connection.Connection;
import com.chain.autostoragesystem.api.connection.IConnection;
import com.chain.autostoragesystem.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Соединяющий кабель
 */
public class LinkCableEntity extends BaseBlockEntity {

    private final IConnection connection = new Connection(this);


    public LinkCableEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.LINK_CABLE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);

        registerCapability(ModCapabilities.CONNECTION, () -> connection);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        registerCapability(ModCapabilities.CONNECTION, () -> connection);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

}
