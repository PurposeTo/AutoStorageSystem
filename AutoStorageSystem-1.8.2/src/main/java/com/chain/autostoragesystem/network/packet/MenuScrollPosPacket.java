package com.chain.autostoragesystem.network.packet;

import com.chain.autostoragesystem.screen.custom.export_bus.ExportBusMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class MenuScrollPosPacket {

    private final float scrollPos;

    public MenuScrollPosPacket(float scrollPos) {
        this.scrollPos = scrollPos;
    }

    public MenuScrollPosPacket(FriendlyByteBuf buf) {
        this.scrollPos = buf.readFloat();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(scrollPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        final var success = new AtomicBoolean(false);
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(context.getDirection() == NetworkDirection.PLAY_TO_SERVER){
                // HERE WE ARE ON THE SERVER!
                ServerPlayer sender = context.getSender();
                if (sender.containerMenu instanceof ExportBusMenu exportBusMenu) {
                    exportBusMenu.scrollTo(scrollPos);
                    success.set(true);
                }
            }
        });
        return success.get();
    }


}
