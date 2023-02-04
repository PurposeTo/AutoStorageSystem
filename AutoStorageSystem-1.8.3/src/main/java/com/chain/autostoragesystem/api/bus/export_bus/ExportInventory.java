package com.chain.autostoragesystem.api.bus.export_bus;

import com.chain.autostoragesystem.api.bus.filters.IInventoryFilters;
import com.chain.autostoragesystem.api.wrappers.item_handler.IItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.item_handler.ItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import com.chain.autostoragesystem.utils.minecraft.NamesUtil;
import lombok.Setter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExportInventory {

    private final ItemHandlerWrapper inventory;

    @Setter
    private IInventoryFilters filters;

    @Setter
    private IItemHandlerWrapper storagesGroup;

    public ExportInventory(@Nonnull ItemHandlerWrapper inventory,
                           @Nonnull IInventoryFilters filters,
                           @Nonnull IItemHandlerWrapper storagesGroup) {
        this.inventory = inventory;
        this.filters = filters;
        this.storagesGroup = storagesGroup;
    }

    public void tick() {
        Map<Item, List<IStackInSlot>> validItemsInStorages = findValidItems();
        List<Item> itemTypes = validItemsInStorages.keySet().stream().toList();
        //todo получить из мапы и отсортировать?
        List<IStackInSlot> stacks = new ArrayList<>();

        tryExportFromSystem(itemTypes, stacks);
    }

    private Map<Item, List<IStackInSlot>> findValidItems() {
        return storagesGroup.getNotEmptyStacks()
                .stream()
                .collect(Collectors.groupingBy(IStackInSlot::getItem))
                .entrySet()
                .stream()
                .filter(entry -> {
                    Item itemType = entry.getKey();
                    return canHoldItem(itemType);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void tryExportFromSystem(@Nonnull List<Item> itemTypes, List<IStackInSlot> stacks) {
        List<Item> ItemTypesToExport = new ArrayList<>(itemTypes);

        for (IStackInSlot stack : stacks) {
            Item itemTypeToExtract = stack.getItem();
            if (!ItemTypesToExport.contains(itemTypeToExtract)) {
                continue;
            }

            ItemStack remaining = stack.moveItemStack(this.storagesGroup);
            if (!remaining.isEmpty()) {
                ItemTypesToExport.remove(remaining.getItem());
            }
        }
    }

    /**
     * Может ли в инвентаре находиться данный предмет
     */
    private boolean canHoldItem(@Nonnull Item item) {
        return this.filters.canHoldItem(item);
    }

    private boolean hasAmountLimit(@Nonnull Item item) {
        return this.filters.hasAmountLimit(item);
    }

    private int getAmountLimit(@Nonnull Item item) {
        if (!hasAmountLimit(item)) {
            throw new IllegalArgumentException("Item " + NamesUtil.getItemName(item) + " has not amount limit");
        }

        return this.filters.getAmountLimit(item);
    }
}
