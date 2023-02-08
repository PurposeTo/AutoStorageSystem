package com.chain.autostoragesystem.datagen;

import com.chain.autostoragesystem.ModMain;
import com.chain.autostoragesystem.block.ModBlocks;
import com.chain.autostoragesystem.item.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String locale) {
        super(gen, ModMain.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        this.add(ModItems.CITRINE.get(), "Citrine");
        this.add(ModItems.RAW_CITRINE.get(), "Raw citrine");

        this.add(ModBlocks.CITRINE_BLOCK.get(), "Citrine block");

        this.add(ModBlocks.SYSTEM_CONTROLLER_BLOCK.get(), "System controller");
        this.add(ModBlocks.IMPORT_BUS_BLOCK.get(), "Import bus");
        this.add(ModBlocks.EXPORT_BUS_BLOCK.get(), "Export bus");

        this.add("item." + ModMain.MOD_ID + ".debug_item.no_valuables", "No blocks found!");
    }
}
