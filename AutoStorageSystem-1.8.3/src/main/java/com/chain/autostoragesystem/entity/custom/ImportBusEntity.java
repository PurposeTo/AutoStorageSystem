package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.api.NeighborsApi;
import com.chain.autostoragesystem.api.bus.import_bus.ImportBus;
import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.utils.minecraft.Levels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ImportBusEntity extends BlockEntity {

    private final ImportBus importBus;

    // Список хранит строго существующие IItemHandler-ы
    List<IItemHandler> connectedInventories = new ArrayList<>();

    LazyOptional<IImportBus> importBusLazyOptional;

    public ImportBusEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.IMPORT_BUS_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);

        importBus = new ImportBus(pWorldPosition);
        this.importBusLazyOptional = LazyOptional.of(() -> importBus);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        importBusLazyOptional = LazyOptional.of(() -> importBus);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, ImportBusEntity blockEntity) {
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ImportBusEntity blockEntity) {
        Levels.requireServerSide(level);

        blockEntity.connectedInventories = NeighborsApi.getItemHandlers(level, pos);
        blockEntity.updateInventoryBusState();
    }

    private void updateInventoryBusState() {
        importBus.setConnectedInventories(this.connectedInventories);
    }


    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ModCapabilities.IMPORT_BUS_CAPABILITY) {
            return importBusLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        importBusLazyOptional.invalidate();
    }
}
