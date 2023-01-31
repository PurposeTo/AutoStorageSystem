package com.chain.autostoragesystem.api;

import lombok.Setter;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ImportBus implements IImportBus {

    private Set<IItemHandler> connectedInventories = new HashSet<>();

    @Override
    public Set<IItemHandler> getConnectedInventories() {
        return this.connectedInventories;
    }

    @Override
    public Set<ImportRequest> getImportRequests() {
        return this.connectedInventories.stream()
                .flatMap(inventory -> getImportRequest(inventory).stream())
                .collect(Collectors.toSet());
    }

    public void setConnectedInventories(@Nonnull Set<IItemHandler> inventories) {
        this.connectedInventories = inventories;
    }

    //todo учитывать фильтры
    private Set<ImportRequest> getImportRequest(IItemHandler inventory) {
        Set<ImportRequest> requests = new HashSet<>();

        int slots = inventory.getSlots();
        for (int slot = 0; slot < slots; slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);

            if (!stack.isEmpty()) {
                requests.add(new ImportRequest(inventory, stack, slot));
            }
        }

        return requests;
    }
}
