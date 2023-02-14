package com.chain.autostoragesystem.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ExamplePacket {

    private final String str;

    public ExamplePacket(String str) {
        this.str = str;
    }

    public ExamplePacket(FriendlyByteBuf buf) {
        str = "I am from extraData";
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        final var success = new AtomicBoolean();
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                // HERE WE ARE ON THE CLIENT!
                System.out.println(str + " КРЯ");
                success.set(true);
            });
        });
        return success.get();
    }


}
