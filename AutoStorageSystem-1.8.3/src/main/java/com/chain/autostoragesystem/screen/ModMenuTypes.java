package com.chain.autostoragesystem.screen;

import com.chain.autostoragesystem.ModMain;
import com.chain.autostoragesystem.screen.custom.ExportBusMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, ModMain.MOD_ID);

    public static final RegistryObject<MenuType<ExportBusMenu>> EXPORT_BUS_MENU =
            registerMenuType(ExportBusMenu::new, "export_bus_menu");


    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void registerAll(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
