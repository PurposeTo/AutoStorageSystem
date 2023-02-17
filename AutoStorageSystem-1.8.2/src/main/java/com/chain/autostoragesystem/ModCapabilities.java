package com.chain.autostoragesystem;

import com.chain.autostoragesystem.api.bus.export_bus.IExportBus;
import com.chain.autostoragesystem.api.bus.import_bus.IImportBus;
import com.chain.autostoragesystem.api.bus.storage_bus.IStorageBus;
import com.chain.autostoragesystem.api.connection.IConnection;
import net.minecraft.world.SimpleContainer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCapabilities {

    public static final Capability<SimpleContainer> CONTAINER = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<IConnection> CONNECTION = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<IImportBus> IMPORT_BUS = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<IExportBus> EXPORT_BUS = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<IStorageBus> STORAGE_BUS = CapabilityManager.get(new CapabilityToken<>() {
    });

    @SubscribeEvent
    public static void initCapabilities(RegisterCapabilitiesEvent event) {
        event.register(SimpleContainer.class);
        event.register(IConnection.class);
        event.register(IImportBus.class);
        event.register(IExportBus.class);
        event.register(IStorageBus.class);
    }
}
