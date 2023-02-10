package com.chain.autostoragesystem.api.bus;

import com.chain.autostoragesystem.api.storage_system.Config;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerComparer;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBus<T extends IItemHandlerComparer & IConfigurable> implements IConfigurable {

    @Nonnull
    protected final List<T> inventories = new ArrayList<>();

    @Nonnull
    private Config config = Config.getDefault();

    public abstract void tick();

    public void updateInventories(@Nonnull List<IItemHandlerWrapper> inventories) {
        List<T> invalidInventories = this.inventories
                .stream()
                .filter(exportInv -> inventories.stream().noneMatch(exportInv::same))
                .toList();
        this.inventories.removeAll(invalidInventories);

        var newInventories = inventories
                .stream()
                .filter(itemHandler -> this.inventories.stream().noneMatch(exportInventory -> exportInventory.same(itemHandler)))
                .map(itemHandler -> createNewT(itemHandler, config))
                .toList();
        this.inventories.addAll(newInventories);
    }

    protected abstract T createNewT(@Nonnull IItemHandlerWrapper inventory, @Nonnull Config config);

    @Override

    public void setConfig(@Nonnull Config config) {
        this.config = config;
        inventories.forEach(inventory -> inventory.setConfig(config));
    }
}