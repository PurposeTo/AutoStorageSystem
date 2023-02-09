package com.chain.autostoragesystem.api.wrappers.item_handler;

import javax.annotation.Nonnull;

public interface IItemHandlerComparer {
    boolean same(@Nonnull IItemHandlerWrapper itemHandler);
}
