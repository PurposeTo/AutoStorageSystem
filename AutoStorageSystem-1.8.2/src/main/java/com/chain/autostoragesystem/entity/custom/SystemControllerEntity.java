package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.api.connection.Connection;
import com.chain.autostoragesystem.api.connection.IConnection;
import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.utils.minecraft.Levels;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

/**
 * Блок, управляющий системой
 */
public class SystemControllerEntity extends BaseBlockEntity {

    private final IConnection connection = new Connection(this);


    public SystemControllerEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.SYSTEM_CONTROLLER_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);

        registerCapability(ModCapabilities.CONNECTION_CAPABILITY, LazyOptional.of(() -> connection));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        registerCapability(ModCapabilities.CONNECTION_CAPABILITY, LazyOptional.of(() -> connection));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, SystemControllerEntity blockEntity) {
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SystemControllerEntity blockEntity) {
        Levels.requireServerSide(level);
    }


}
