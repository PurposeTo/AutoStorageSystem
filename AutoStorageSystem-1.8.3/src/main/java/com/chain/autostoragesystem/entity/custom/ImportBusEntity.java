package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.api.IImportBus;
import com.chain.autostoragesystem.api.ImportBus;
import com.chain.autostoragesystem.api.ImportRequest;
import com.chain.autostoragesystem.api.NeighborsApi;
import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.utils.common.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ImportBusEntity extends BlockEntity {

    private final int id = Random.range(0, 1000000);

    private final ImportBus importBus = new ImportBus();

    List<IItemHandler> connectedInventories = new ArrayList<>();

    LazyOptional<IImportBus> importBusLazyOptional = LazyOptional.of(() -> importBus);

    public ImportBusEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.IMPORT_BUS_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
        System.out.println("Created entity with id: " + id);
        Thread thread = Thread.currentThread();
        StackTraceElement[] stackTraceElements = thread.getStackTrace();
        String stackTrace = Arrays.stream(stackTraceElements)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
        System.out.println(stackTrace);
        System.out.println("---");
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

    public static void tick(Level level, BlockPos pos, BlockState state, ImportBusEntity blockEntity) {
        if (!level.isClientSide()) {
            blockEntity.connectedInventories = NeighborsApi.getItemHandlers(level, pos);
            blockEntity.updateInventoryBusState();
        }
    }

    private void updateInventoryBusState() {
        importBus.setConnectedInventories(new HashSet<>(this.connectedInventories));
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
