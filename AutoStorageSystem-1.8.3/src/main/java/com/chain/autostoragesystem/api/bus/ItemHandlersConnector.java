package com.chain.autostoragesystem.api.bus;

import com.chain.autostoragesystem.utils.common.CollectionUtils;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemHandlersConnector {

    // Список хранит строго существующие IItemHandler-ы
    protected List<IItemHandler> connectedInventories = new ArrayList<>();

    public void setConnectedInventories(@Nonnull List<IItemHandler> inventories) {
        if (!CollectionUtils.equalObjectsReferences(this.connectedInventories, inventories)) {
            this.connectedInventories = inventories;
        }
    }
}
