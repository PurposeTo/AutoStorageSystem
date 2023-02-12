package com.chain.autostoragesystem.api.wrappers.item_handler;

import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.items_transmitter.IItemsTransmitter;
import net.minecraftforge.items.IItemHandler;

public interface IItemHandlerWrapper extends IItemsReceiver, IItemsTransmitter {
    IItemHandler unwrap();

}
