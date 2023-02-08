package com.chain.autostoragesystem;

import com.chain.autostoragesystem.api.bus.export_bus.IExportBus;
import com.chain.autostoragesystem.api.bus.import_bus.IImportBus;
import net.minecraft.world.Container;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCapabilities {

    public static final Capability<IImportBus> IMPORT_BUS_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<IExportBus> EXPORT_BUS_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final Capability<Container> CONTAINER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    @SubscribeEvent
    public static void initCapabilities(RegisterCapabilitiesEvent event) {
        event.register(Container.class);
        event.register(IImportBus.class);
        event.register(IExportBus.class);
    }
}
