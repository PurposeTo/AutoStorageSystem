package com.chain.autostoragesystem.api.bus.export_bus;

import com.chain.autostoragesystem.api.wrappers.items_transmitter.IItemsTransmitter;

import javax.annotation.Nonnull;

public interface IExportBus {
    void setItemsTransmitter(@Nonnull IItemsTransmitter itemsTransmitter);
}
