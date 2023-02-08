package com.chain.autostoragesystem.item;

import com.chain.autostoragesystem.ModMain;
import com.chain.autostoragesystem.item.custom.DebugItem;
import com.chain.autostoragesystem.itemGroup.ModCreativeModeTab;
import com.chain.autostoragesystem.utils.common.StringUtils;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Supplier;

public class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModMain.MOD_ID);

    public static final RegistryObject<Item> DEBUG_ITEM = registerItem(
            "debug_item",
            () -> new DebugItem(ModCreativeModeTab.MODE_TAB));

    public static final RegistryObject<Item> CITRINE = registerItem(
            "citrine",
            () -> new DefaultItem(ModCreativeModeTab.MODE_TAB));

    public static final RegistryObject<Item> RAW_CITRINE = registerItem(
            "raw_citrine",
            () -> new DefaultItem(ModCreativeModeTab.MODE_TAB));


    public static void registerAll(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static RegistryObject<Item> registerItem(
            String name,
            Supplier<? extends Item> sup) {
        StringUtils.requiredNonBlank(name);
        Objects.requireNonNull(sup);

        return ITEMS.register(name, sup);
    }
}
