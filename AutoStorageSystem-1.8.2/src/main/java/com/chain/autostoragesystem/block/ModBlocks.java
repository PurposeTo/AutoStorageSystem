package com.chain.autostoragesystem.block;

import com.chain.autostoragesystem.ModMain;
import com.chain.autostoragesystem.block.custom.ExportBusBlock;
import com.chain.autostoragesystem.block.custom.ImportBusBlock;
import com.chain.autostoragesystem.block.custom.base.BlockWithEntity;
import com.chain.autostoragesystem.entity.custom.LinkCableEntity;
import com.chain.autostoragesystem.entity.custom.StorageBusEntity;
import com.chain.autostoragesystem.item.ModItems;
import com.chain.autostoragesystem.itemGroup.ModCreativeModeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

// Здесь необходимо зарегистрировать блоки, которые добавляет мод.
// Блоки автоматически регистрируются как айтемы.
public class ModBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModMain.MOD_ID);

    public static final RegistryObject<Block> CITRINE_BLOCK = blockWithItem("citrine_block", () -> new Block(getBlockProp()));

    public static final RegistryObject<Block> LINK_CABLE_BLOCK = blockWithItem("link_cable_block", () -> new BlockWithEntity(getBlockProp(), LinkCableEntity::new));
    public static final RegistryObject<Block> IMPORT_BUS_BLOCK = blockWithItem("import_bus_block", () -> new ImportBusBlock(getBlockProp()));
    public static final RegistryObject<Block> EXPORT_BUS_BLOCK = blockWithItem("export_bus_block", () -> new ExportBusBlock(getBlockProp()));
    public static final RegistryObject<Block> STORAGE_BUS_BLOCK = blockWithItem("storage_bus_block", () -> new BlockWithEntity(getBlockProp(), StorageBusEntity::new));


    public static void registerAll(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static <B extends Block> RegistryObject<B> blockWithItem(String name, Supplier<B> create) {
        return blockWithItem(name, create, (rb) -> new BlockItem(rb, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB)));
    }

    private static <B extends Block, I extends Item> RegistryObject<B> blockWithItem(String name, Supplier<B> create, Function<Block, I> createItem) {
        RegistryObject<B> re = BLOCKS.register(name, create);
        ModItems.registerItem(name, () -> createItem.apply(re.get()));
        return re;
    }

    private static BlockBehaviour.Properties getBlockProp() {
        return BlockBehaviour.Properties.of(Material.METAL)
                .strength(9f)
                .requiresCorrectToolForDrops();
    }
}
