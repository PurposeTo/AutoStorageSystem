package com.chain.autostoragesystem.network.packet.base;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class S2CPacket {

    /**
     * use this to create a packet to send
     */
    public S2CPacket() {

    }

    /**
     * use this to create a packet from receive param [buf]
     */
    public S2CPacket(FriendlyByteBuf buf) {
        this();
    }

    /**
     * use this to save data fields to [buf]
     */
    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        final var success = new AtomicBoolean();
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                // HERE WE ARE ON THE CLIENT!
                success.set(true);
            });
        });
        return success.get();
    }


}