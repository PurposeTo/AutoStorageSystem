package com.chain.autostoragesystem;

import com.chain.autostoragesystem.api.bus.import_bus.IImportBus;
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

    @SubscribeEvent
    public static void initCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IImportBus.class);
    }
}
