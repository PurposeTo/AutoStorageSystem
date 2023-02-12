package com.chain.autostoragesystem.entity.custom;

import com.chain.autostoragesystem.ModCapabilities;
import com.chain.autostoragesystem.api.bus.export_bus.ExportBus;
import com.chain.autostoragesystem.api.bus.filters.ItemTypeFiltersFactory;
import com.chain.autostoragesystem.api.connection.Connection;
import com.chain.autostoragesystem.api.wrappers.ItemHandlerGroup;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import com.chain.autostoragesystem.entity.ModBlockEntities;
import com.chain.autostoragesystem.entity.custom.base.BaseBlockEntity;
import com.chain.autostoragesystem.screen.custom.ExportBusMenu;
import com.chain.autostoragesystem.utils.minecraft.TickerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExportBusEntity extends BaseBlockEntity implements TickerUtil.TickableServer, MenuProvider {
    private final Connection connection = new Connection(this);
    private final SimpleContainer filters = new SimpleContainer(27);

    private final ExportBus exportBus;

    private final ItemHandlerGroup itemsTransmitter = new ItemHandlerGroup();


    public ExportBusEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.EXPORT_BUS_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);

        exportBus = new ExportBus(itemsTransmitter, ItemTypeFiltersFactory.getFromContainer(filters));
        registerCapability(ModCapabilities.CONNECTION, () -> connection);
        registerCapability(ModCapabilities.CONTAINER, () -> filters);
        registerCapability(ModCapabilities.EXPORT_BUS, () -> exportBus);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        registerCapability(ModCapabilities.CONNECTION, () -> connection);
        registerCapability(ModCapabilities.CONTAINER, () -> filters);
        registerCapability(ModCapabilities.EXPORT_BUS, () -> exportBus);
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

    @Override
    public void tickServer() {
        List<IItemHandlerWrapper> storageInventories = connection.getStorageBusses()
                .stream()
                .flatMap(storageBus -> storageBus.getStorageItemHandlers().stream())
                .toList();
        itemsTransmitter.resetItemHandlers(storageInventories);

        List<IItemHandlerWrapper> connectedInventories = connection.getNeighboursItemHandlers();
        exportBus.updateInventories(connectedInventories);

        exportBus.tick();
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
