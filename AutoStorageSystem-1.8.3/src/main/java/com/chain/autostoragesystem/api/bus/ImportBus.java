package com.chain.autostoragesystem.api.bus;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ImportBus extends ItemHandlersConnector implements IImportBus {

    @Getter
    @Nonnull
    private final BlockPos pos;

    public ImportBus(@Nonnull BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public Set<ImportRequest> getImportRequests() {
        return this.connectedInventories.stream()
                .flatMap(inventory -> getImportRequest(inventory).stream())
                .collect(Collectors.toSet());
    }

    //todo учитывать фильтры
    //todo должен быть отсортирован по увеличению номера слота
    private List<ImportRequest> getImportRequest(IItemHandler inventory) {
        List<ImportRequest> requests = new ArrayList<>();

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
