package com.chain.autostoragesystem.api.bus;

import lombok.Getter;
import net.minecraft.core.BlockPos;

import javax.annotation.Nonnull;

public abstract class AbstractBus extends ItemHandlersConnector {

    @Getter
    @Nonnull
    protected final BlockPos pos;

    protected AbstractBus(@Nonnull BlockPos pos) {
        this.pos = pos;
    }

    public abstract void tick();


}