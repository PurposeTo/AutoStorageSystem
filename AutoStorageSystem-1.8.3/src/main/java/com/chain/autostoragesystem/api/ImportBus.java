package com.chain.autostoragesystem.api;

import com.chain.autostoragesystem.utils.common.CollectionUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ImportBus implements IImportBus {


    // LinkedHashSet чтобы не хранить дубликаты и иметь очередность
    // Список хранит строго существующие IItemHandler-ы
    private LinkedHashSet<IItemHandler> connectedInventories = new LinkedHashSet<>();

    @Override
    public Set<ImportRequest> getImportRequests() {
        return this.connectedInventories.stream()
                .flatMap(inventory -> getImportRequest(inventory).stream())
                .collect(Collectors.toSet());
    }

    public void setConnectedInventories(@Nonnull LinkedHashSet<IItemHandler> inventories) {
        if (!CollectionUtils.equalObjectsReferences(this.connectedInventories, inventories)) {
            this.connectedInventories = inventories;
        }
    }

    //todo учитывать фильтры
    private LinkedHashSet<ImportRequest> getImportRequest(IItemHandler inventory) {
        LinkedHashSet<ImportRequest> requests = new LinkedHashSet<>();

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
