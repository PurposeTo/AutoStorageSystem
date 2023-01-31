package com.chain.autostoragesystem.datagen;

import com.chain.autostoragesystem.ModMain;
import com.chain.autostoragesystem.block.ModBlocks;
import com.chain.autostoragesystem.item.ModItems;
import com.chain.autostoragesystem.utils.common.StringUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;


// Здесь необходимо зарегестрировать все айтемы, которые добавляет мод
// для инициализации их текстур
public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ModMain.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        blockItem(ModBlocks.CITRINE_BLOCK.get());

        blockItem(ModBlocks.IMPORT_BUS_BLOCK.get());
        blockItem(ModBlocks.SYSTEM_CONTROLLER_BLOCK.get());

        simpleItem(ModItems.CITRINE.get());
        simpleItem(ModItems.RAW_CITRINE.get());

        handheldItem(ModItems.DEBUG_ITEM.get());
    }

    private ItemModelBuilder blockItem(Block block) {
        String registryName = StringUtils.requiredNonBlank(block.getRegistryName().getPath());

        return withExistingParent(registryName,
                new ResourceLocation(ModMain.MOD_ID, "block/" + registryName));
    }

    private ItemModelBuilder simpleItem(Item item) {
        String registryName = StringUtils.requiredNonBlank(item.getRegistryName().getPath());

        return withExistingParent(registryName,
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ModMain.MOD_ID, "item/" + registryName));
    }

    private ItemModelBuilder handheldItem(Item item) {
        String registryName = StringUtils.requiredNonBlank(item.getRegistryName().getPath());

        return withExistingParent(registryName,
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(ModMain.MOD_ID, "item/" + registryName));
    }
}
