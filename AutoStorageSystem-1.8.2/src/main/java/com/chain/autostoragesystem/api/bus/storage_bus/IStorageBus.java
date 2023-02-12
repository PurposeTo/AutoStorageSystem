package com.chain.autostoragesystem.api.bus.storage_bus;

import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;

import javax.annotation.Nonnull;
import java.util.List;

public interface IStorageBus {

    @Nonnull
    List<IItemHandlerWrapper> getStorageItemHandlers();

}
