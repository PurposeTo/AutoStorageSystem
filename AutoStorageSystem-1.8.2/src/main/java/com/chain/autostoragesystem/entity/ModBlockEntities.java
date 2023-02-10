package com.chain.autostoragesystem.entity;

import com.chain.autostoragesystem.ModMain;
import com.chain.autostoragesystem.block.ModBlocks;
import com.chain.autostoragesystem.entity.custom.ExportBusEntity;
import com.chain.autostoragesystem.entity.custom.ImportBusEntity;
import com.chain.autostoragesystem.entity.custom.LinkCableEntity;
import com.chain.autostoragesystem.entity.custom.StorageBusEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ModMain.MOD_ID);

    public static final RegistryObject<BlockEntityType<LinkCableEntity>> LINK_CABLE_BLOCK_ENTITY = registerBlockEntity("link_cable_block_entity", LinkCableEntity::new, ModBlocks.LINK_CABLE_BLOCK);
    public static final RegistryObject<BlockEntityType<ImportBusEntity>> IMPORT_BUS_BLOCK_ENTITY = registerBlockEntity("import_bus_block_entity", ImportBusEntity::new, ModBlocks.IMPORT_BUS_BLOCK);
    public static final RegistryObject<BlockEntityType<ExportBusEntity>> EXPORT_BUS_BLOCK_ENTITY = registerBlockEntity("export_bus_block_entity", ExportBusEntity::new, ModBlocks.EXPORT_BUS_BLOCK);
    public static final RegistryObject<BlockEntityType<StorageBusEntity>>STORAGE_BUS_BLOCK_ENTITY = registerBlockEntity("storage_bus_block_entity", StorageBusEntity::new, ModBlocks.STORAGE_BUS_BLOCK);


    public static void registerAll(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    @SafeVarargs
    private static <BE extends BlockEntity> RegistryObject<BlockEntityType<BE>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<? extends BE> create, RegistryObject<? extends Block>... blocks) {
        return BLOCK_ENTITIES.register(name, () -> {
            return BlockEntityType.Builder.<BE>of(create, Arrays.stream(blocks).map(RegistryObject::get).toArray(Block[]::new)).build(null);
        });
    }
}