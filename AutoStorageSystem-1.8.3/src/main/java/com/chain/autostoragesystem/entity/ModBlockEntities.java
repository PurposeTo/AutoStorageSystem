package com.chain.autostoragesystem.entity;

import com.chain.autostoragesystem.ModMain;
import com.chain.autostoragesystem.block.ModBlocks;
import com.chain.autostoragesystem.entity.custom.ExportBusEntity;
import com.chain.autostoragesystem.entity.custom.ImportBusEntity;
import com.chain.autostoragesystem.entity.custom.SystemControllerEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ModMain.MOD_ID);

    public static final RegistryObject<BlockEntityType<SystemControllerEntity>> SYSTEM_CONTROLLER_BLOCK_ENTITY = registerBlockEntity(
            "system_controller_block_entity",
            () -> BlockEntityType.Builder.of(SystemControllerEntity::new, ModBlocks.SYSTEM_CONTROLLER_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<ImportBusEntity>> IMPORT_BUS_BLOCK_ENTITY = registerBlockEntity(
            "import_bus_block_entity",
            () -> BlockEntityType.Builder.of(ImportBusEntity::new, ModBlocks.IMPORT_BUS_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<ExportBusEntity>> EXPORT_BUS_BLOCK_ENTITY = registerBlockEntity(
            "export_bus_block_entity",
            () -> BlockEntityType.Builder.of(ExportBusEntity::new, ModBlocks.EXPORT_BUS_BLOCK.get()).build(null));


    public static void registerAll(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    // why this don't work? Minecraft starting exception
    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> pFactory, Block... pValidBlocks) {
        return registerBlockEntity(
                name,
                () -> BlockEntityType.Builder.of(pFactory, pValidBlocks).build(null));
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntity(String name,
                                                                                                  Supplier<? extends BlockEntityType<T>> sup) {
        return BLOCK_ENTITIES.register(name, sup);
    }
}