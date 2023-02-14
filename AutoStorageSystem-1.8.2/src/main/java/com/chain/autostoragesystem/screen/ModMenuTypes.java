package com.chain.autostoragesystem.screen;

import com.chain.autostoragesystem.ModMain;
import com.chain.autostoragesystem.screen.custom.export_bus.ExportBusMenu;
import com.chain.autostoragesystem.screen.custom.storage_terminal.StorageTerminalMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, ModMain.MOD_ID);

    public static final RegistryObject<MenuType<ExportBusMenu>> EXPORT_BUS_MENU =
            registerMenuType("export_bus_menu", ExportBusMenu::new);
    public static final RegistryObject<MenuType<StorageTerminalMenu>> STORAGE_TERMINAL_MENU =
            registerMenuType("storage_terminal_menu", StorageTerminalMenu::new);


    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(
            String name,
            IContainerFactory<T> factory) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void registerAll(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
