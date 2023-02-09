package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.api.bus.import_bus.ImportBus;
import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.utils.minecraft.Levels;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ImportBusEntity extends BaseBlockEntity {

    private final ImportBus importBus;

    // Список хранит строго существующие IItemHandler-ы
    List<IItemHandler> connectedInventories = new ArrayList<>();


    public ImportBusEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.IMPORT_BUS_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);

        importBus = new ImportBus(pWorldPosition);
        registerCapability(ModCapabilities.IMPORT_BUS_CAPABILITY, LazyOptional.of(() -> importBus));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        registerCapability(ModCapabilities.IMPORT_BUS_CAPABILITY, LazyOptional.of(() -> importBus));
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

//        blockEntity.connectedInventories = NeighborsApi.getItemHandlers(level, pos);
//        blockEntity.updateInventories();
//        blockEntity.importBus.tick();
    }

    private void updateInventories() {
        importBus.setConnectedInventories(this.connectedInventories);
    }

}
