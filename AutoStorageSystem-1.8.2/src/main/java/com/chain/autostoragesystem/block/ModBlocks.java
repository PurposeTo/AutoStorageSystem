package com.chain.autostoragesystem.block;

import com.chain.autostoragesystem.ModMain;
import com.chain.autostoragesystem.block.custom.ExportBusBlock;
import com.chain.autostoragesystem.block.custom.ImportBusBlock;
import com.chain.autostoragesystem.block.custom.SystemController;
import com.chain.autostoragesystem.item.ModItems;
import com.chain.autostoragesystem.itemGroup.ModCreativeModeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

// Здесь необходимо зарегистрировать блоки, которые добавляет мод.
// Блоки автоматически регистрируются как айтемы.
public class ModBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModMain.MOD_ID);

    public static final RegistryObject<Block> CITRINE_BLOCK = registerBlock(
            "citrine_block",
            () -> new Block(getBlockProp()),
            getItemProp(ModCreativeModeTab.MODE_TAB));

    public static final RegistryObject<Block> SYSTEM_CONTROLLER_BLOCK = registerBlock(
            "system_controller_block",
            () -> new SystemController(getBlockProp()),
            getItemProp(ModCreativeModeTab.MODE_TAB));

    public static final RegistryObject<Block> IMPORT_BUS_BLOCK = registerBlock(
            "import_bus_block",
            () -> new ImportBusBlock(getBlockProp()),
            getItemProp(ModCreativeModeTab.MODE_TAB));

    public static final RegistryObject<Block> EXPORT_BUS_BLOCK = registerBlock(
            "export_bus_block",
            () -> new ExportBusBlock(getBlockProp()),
            getItemProp(ModCreativeModeTab.MODE_TAB));


    public static void registerAll(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    public static RegistryObject<Block> registerBlock(String name, Supplier<? extends Block> block, Item.Properties properties) {
        RegistryObject<Block> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, properties);
        return toReturn;
    }

    private static RegistryObject<Item> registerBlockItem(String name,
                                                          RegistryObject<? extends Block> block,
                                                          Item.Properties properties) {
        return ModItems.registerItem(
                name,
                () -> new BlockItem(block.get(), properties)
        );
    }


    private static BlockBehaviour.Properties getBlockProp() {
        return BlockBehaviour.Properties.of(Material.METAL)
                .strength(9f)
                .requiresCorrectToolForDrops();
    }

    private static Item.Properties getItemProp(CreativeModeTab tab) {
        return new Item.Properties()
                .tab(tab);
    }
}
