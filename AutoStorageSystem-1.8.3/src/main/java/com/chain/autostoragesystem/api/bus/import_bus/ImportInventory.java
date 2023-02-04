package com.chain.autostoragesystem.api.bus.import_bus;

import com.chain.autostoragesystem.api.bus.filters.IInventoryFilters;
import com.chain.autostoragesystem.api.wrappers.ExtractRequest;
import com.chain.autostoragesystem.api.wrappers.item_handler.ItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import com.chain.autostoragesystem.utils.minecraft.NamesUtil;
import lombok.Setter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;


public class ImportInventory {

    private final ItemHandlerWrapper inventory;

    @Setter
    private IInventoryFilters filters;
    @Setter
    private IItemsReceiver storagesGroup;

    public ImportInventory(@Nonnull IItemHandler inventory,
                           @Nonnull IInventoryFilters filters,
                           @Nonnull IItemsReceiver storagesGroup) {
        this.inventory = new ItemHandlerWrapper(inventory);
        this.filters = filters;
        this.storagesGroup = storagesGroup;
    }

    public void tick() {
        Map<Item, List<ExtractRequest>> invalidItemsMap = findInvalidItems();
        List<Item> itemTypes = invalidItemsMap.keySet().stream().toList();

        List<ExtractRequest> extractRequests = invalidItemsMap.values()
                .stream()
                .flatMap(List::stream)
                .sorted(Comparator.comparing(it -> it.getStackInSlot().getSlot()))
                .collect(Collectors.toList());

        tryImportToSystem(itemTypes, extractRequests);
    }

    /**
     * Получить стаки, сгруппированные по типу предмета, которые должны быть убраны из инвентаря.
     */
    //todo что делать с предметами, у которых есть свойство damaged?
    //todo есть ли смысл получать список всех предметов, если его все равно надо ограничить по количеству? Нужно ли сделать оптимизацию?
    private Map<Item, List<ExtractRequest>> findInvalidItems() {

        List<IStackInSlot> invalidStacks = inventory.getNotEmptyStacks();

        Map<Item, List<IStackInSlot>> notEmptyStacksGrouped = invalidStacks
                .stream()
                .collect(Collectors.groupingBy(IStackInSlot::getItem));

        Map<Item, List<ExtractRequest>> stacksWithInvalidItemType = notEmptyStacksGrouped
                .entrySet()
                .stream()
                .filter(entry -> {
                    Item itemType = entry.getKey();
                    return !canHoldItem(itemType);
                })
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> {
                            List<IStackInSlot> stacksSameType = entry.getValue();

                            return stacksSameType
                                    .stream()
                                    .map(ExtractRequest::new)
                                    .toList();

                        }));

        Map<Item, List<ExtractRequest>> stacksWithExcessAmount = notEmptyStacksGrouped
                .entrySet()
                .stream()
                .filter(entry -> {
                    Item itemType = entry.getKey();
                    List<IStackInSlot> stacksSameType = entry.getValue();
                    int itemsAmount = stacksSameType
                            .stream()
                            .mapToInt(inventoryStack -> inventoryStack.resolve().getCount())
                            .sum();

                    return canHoldItem(itemType) && // можно хранить
                            hasAmountLimit(itemType) && // есть лимит на хранение
                            itemsAmount > getAmountLimit(itemType); // лимит превышен
                })
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> {
                            Item itemType = entry.getKey();
                            List<IStackInSlot> stacksSameType = entry.getValue();

                            int itemsSum = stacksSameType.stream()
                                    .mapToInt(inventoryStack -> inventoryStack.resolve().getCount())
                                    .sum();

                            // избыточное количество предметов, лежащих в инвентаре
                            int excessAmount = itemsSum - getAmountLimit(itemType);

                            List<ExtractRequest> excessStacks = new ArrayList<>();
                            for (int i = 0; excessAmount > 0 && i < stacksSameType.size(); i++) {
                                IStackInSlot stack = stacksSameType.get(i);
                                int stackCount = stack.resolve().getCount();

                                if (stackCount <= excessAmount) {
                                    excessStacks.add(new ExtractRequest(stack));
                                    excessAmount -= stackCount;
                                } else {
                                    ExtractRequest extractRequest = new ExtractRequest(stack, excessAmount);
                                    excessStacks.add(extractRequest);
                                    excessAmount = 0;
                                }
                            }

                            return excessStacks;
                        }));

        Map<Item, List<ExtractRequest>> invalidItems = new HashMap<>();
        invalidItems.putAll(stacksWithInvalidItemType);
        invalidItems.putAll(stacksWithExcessAmount);
        return invalidItems;
    }

    /**
     * Попытаться импортировать в систему стаки предметов.
     * Если нет остатка - значит продолжаем.
     * Если есть остаток - значит больше не надо пробовать импортировать в систему предметы данного типа.
     *
     * @param itemTypes       типы предметов, которые будут импортироваться
     * @param extractRequests список:
     *                        стак предметов для импорта, лежащий в инвентаре,
     *                        количество предметов, которые нужно импортироваться
     */
    private void tryImportToSystem(@Nonnull List<Item> itemTypes, List<ExtractRequest> extractRequests) {
        List<Item> ItemTypesToImport = new ArrayList<>(itemTypes);

        for (ExtractRequest extractRequest : extractRequests) {
            Item itemTypeToExtract = extractRequest.getItemType();
            if (!ItemTypesToImport.contains(itemTypeToExtract)) {
                continue;
            }

            ItemStack remaining = extractRequest.moveItemStack(this.storagesGroup);
            if (!remaining.isEmpty()) {
                ItemTypesToImport.remove(remaining.getItem());
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
