package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.api.bus.export_bus.ExportBus;
import com.chain.autostoragesystem.api.bus.filters.ItemTypeFiltersFactory;
import com.chain.autostoragesystem.api.connection.Connection;
import com.chain.autostoragesystem.api.connection.IConnection;
import com.chain.autostoragesystem.api.wrappers.ItemHandlerGroup;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.screen.custom.ExportBusMenu;
import com.chain.autostoragesystem.utils.minecraft.Levels;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExportBusEntity extends BaseBlockEntity implements MenuProvider {
    private final SimpleContainer filters = new SimpleContainer(4);

    private final ExportBus exportBus;

    private final IConnection connection = new Connection(this);

    private final ItemHandlerGroup itemsTransmitter = new ItemHandlerGroup();


    public ExportBusEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.EXPORT_BUS_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);

        exportBus = new ExportBus(new ArrayList<>(), itemsTransmitter, ItemTypeFiltersFactory.getFromContainer(filters));
        registerCapability(ModCapabilities.CONNECTION_CAPABILITY, LazyOptional.of(() -> connection));
        registerCapability(ModCapabilities.CONTAINER_CAPABILITY, LazyOptional.of(() -> filters));
        registerCapability(ModCapabilities.EXPORT_BUS_CAPABILITY, LazyOptional.of(() -> exportBus));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        registerCapability(ModCapabilities.CONNECTION_CAPABILITY, LazyOptional.of(() -> connection));
        registerCapability(ModCapabilities.CONTAINER_CAPABILITY, LazyOptional.of(() -> filters));
        registerCapability(ModCapabilities.EXPORT_BUS_CAPABILITY, LazyOptional.of(() -> exportBus));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("filters", filters.createTag());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        filters.fromTag(nbt.getList("filters", CompoundTag.TAG_COMPOUND));
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, ExportBusEntity blockEntity) {
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ExportBusEntity blockEntity) {
        Levels.requireServerSide(level);

        List<IItemHandlerWrapper> storageInventories = blockEntity.connection.getAllConnections()
                .stream()
                .flatMap(connection -> connection.getNeighboursItemHandlers().stream())
                .toList();
        blockEntity.itemsTransmitter.resetItemHandlers(storageInventories);

        List<IItemHandlerWrapper> connectedInventories = blockEntity.connection.getNeighboursItemHandlers();
        blockEntity.exportBus.updateInventories(connectedInventories);

        blockEntity.exportBus.tick();
    }

    @NotNull
    @Override
    public Component getDisplayName() {
        return new TextComponent("Whitelist filters");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new ExportBusMenu(pContainerId, pInventory, this);
    }
}
