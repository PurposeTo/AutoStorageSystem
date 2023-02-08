package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.api.NeighborsApi;
import com.chain.autostoragesystem.api.bus.export_bus.ExportBus;
import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.screen.custom.ExportBusMenu;
import com.chain.autostoragesystem.utils.minecraft.Levels;
import com.chain.autostoragesystem.utils.minecraft.NamesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExportBusEntity extends BaseBlockEntity implements MenuProvider {
    private final SimpleContainer filters = new SimpleContainer(4) {

        //todo обновить
        @Override
        public boolean stillValid(Player player) {
            return true;
        }
    };

    private final ExportBus exportBus;

    // Список хранит строго существующие IItemHandler-ы
    List<IItemHandler> connectedInventories = new ArrayList<>();


    public ExportBusEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.EXPORT_BUS_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);

        exportBus = new ExportBus(pWorldPosition);
        registerCapability(ModCapabilities.CONTAINER_CAPABILITY, LazyOptional.of(() -> filters));
        registerCapability(ModCapabilities.EXPORT_BUS_CAPABILITY, LazyOptional.of(() -> exportBus));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        registerCapability(ModCapabilities.CONTAINER_CAPABILITY, LazyOptional.of(() -> filters));
        registerCapability(ModCapabilities.EXPORT_BUS_CAPABILITY, LazyOptional.of(() -> exportBus));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, ExportBusEntity blockEntity) {
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ExportBusEntity blockEntity) {
        Levels.requireServerSide(level);

        blockEntity.connectedInventories = NeighborsApi.getItemHandlers(level, pos);
        blockEntity.updateInventories();
        blockEntity.exportBus.tick();
    }

    private void updateInventories() {
        exportBus.setConnectedInventories(this.connectedInventories);
    }

    @NotNull
    @Override
    public Component getDisplayName() {
        return new TextComponent(NamesUtil.getBlockEntityName(this));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new ExportBusMenu(pContainerId, pInventory, this);
    }
}
