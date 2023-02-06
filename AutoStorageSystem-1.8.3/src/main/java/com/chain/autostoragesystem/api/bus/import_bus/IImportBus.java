package com.chain.autostoragesystem.api.bus.import_bus;

import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;

import javax.annotation.Nonnull;

public interface IImportBus {

    void setItemsReceiver(@Nonnull IItemsReceiver itemsReceiver);
}
