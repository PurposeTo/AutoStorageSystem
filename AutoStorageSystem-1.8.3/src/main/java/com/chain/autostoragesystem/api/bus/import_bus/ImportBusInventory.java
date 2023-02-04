package com.chain.autostoragesystem.api.bus.import_bus;

import com.chain.autostoragesystem.api.bus.import_filters.IImportFilters;
import com.chain.autostoragesystem.api.wrappers.ExtractRequest;
import com.chain.autostoragesystem.api.wrappers.ItemHandlerWrapper;
import com.chain.autostoragesystem.api.wrappers.items_receiver.IItemsReceiver;
import com.chain.autostoragesystem.api.wrappers.stack_in_slot.IStackInSlot;
import com.chain.autostoragesystem.utils.minecraft.NamesUtil;
import lombok.Setter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ImportBusInventory {

    private final ItemHandlerWrapper inventory;

    @Setter
    private IImportFilters importFilters;
    private final int maxSystemCapacity = 5;
    @Setter
    private IItemsReceiver storageController;

    public ImportBusInventory(@Nonnull IItemHandler inventory,
                              @Nonnull IImportFilters importFilters,
                              @Nonnull IItemsReceiver storageController) {
        this.inventory = new ItemHandlerWrapper(inventory);
        this.importFilters = importFilters;
        this.storageController = storageController;
    }

    public void tick() {
        Map<Item, List<ExtractRequest>> invalidItemsMap = findInvalidItems();
        List<Item> itemTypes = invalidItemsMap.keySet().stream().toList();
        //todo получить из мапы и отсортировать по номеру слота, а также ограничить по maxSystemCapacity
        List<ExtractRequest> extractRequests = new ArrayList<>();

        tryImportToSystem(itemTypes, extractRequests);
    }

    /**
     * Получить стаки, сгруппированные по типу предмета, которые должны быть убраны из инвентаря.
     */
    //todo что делать с предметами, у которых есть свойство damaged?
    //todo есть ли смысл получать список всех предметов, если его все равно надо ограничить по количеству? Нужно ли сделать оптимизацию?
    private Map<Item, List<ExtractRequest>> findInvalidItems() {

        List<IStackInSlot> notEmptyStacks = inventory.getNotEmptyStacks();

        Map<Item, List<IStackInSlot>> groupingStacks = notEmptyStacks
                .stream()
                .collect(Collectors.groupingBy(inventoryStack -> inventoryStack.getItemStack().getItem()));

        Map<Item, List<ExtractRequest>> invalidStacks = new HashMap<>();

        for (Map.Entry<Item, List<IStackInSlot>> entry : groupingStacks.entrySet()) {
            Item itemType = entry.getKey();
            List<IStackInSlot> stacksSameType = entry.getValue();

            boolean canHoldItemType = canHoldItem(itemType);
            if (!canHoldItemType) {
                List<ExtractRequest> extractRequestList = stacksSameType
                        .stream()
                        .map(ExtractRequest::new)
                        .toList();

                invalidStacks.put(itemType, extractRequestList);
                continue;
            }

            if (hasAmountLimit(itemType)) {
                int itemsSum = stacksSameType.stream()
                        .mapToInt(inventoryStack -> inventoryStack.getItemStack().getCount())
                        .sum();

                // избыточное количество предметов, лежащих в инвентаре
                int excessAmount = itemsSum - getAmountLimit(itemType);

                List<ExtractRequest> excessStacks = new ArrayList<>();
                for (int i = 0; excessAmount > 0 && i < stacksSameType.size(); i++) {
                    IStackInSlot stack = stacksSameType.get(i);
                    int stackCount = stack.getItemStack().getCount();

                    if (stackCount <= excessAmount) {
                        excessStacks.add(new ExtractRequest(stack));
                        excessAmount -= stackCount;
                    } else {
                        ExtractRequest extractRequest = new ExtractRequest(stack, excessAmount);
                        excessStacks.add(extractRequest);
                        excessAmount = 0;
                    }
                }

                if (!excessStacks.isEmpty()) {
                    invalidStacks.put(itemType, excessStacks);
                }
            }
        }
        return invalidStacks;
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
        for (ExtractRequest extractRequest : extractRequests) {
            Item itemTypeToExtract = extractRequest.getItemType();
            if (!itemTypes.contains(itemTypeToExtract)) {
                continue;
            }

            ItemStack remaining = extractRequest.moveItemStack(this.storageController);
            if (!remaining.isEmpty()) {
                itemTypes.remove(remaining.getItem());
            }
        }

    }

    /**
     * Может ли в инвентаре находиться данный предмет
     */
    private boolean canHoldItem(@Nonnull Item item) {
        return this.importFilters.canHoldItem(item);
    }

    private boolean hasAmountLimit(@Nonnull Item item) {
        return this.importFilters.hasAmountLimit(item);
    }

    private int getAmountLimit(@Nonnull Item item) {
        if (!hasAmountLimit(item)) {
            throw new IllegalArgumentException("Item " + NamesUtil.getItemName(item) + " has not amount limit");
        }

        return this.importFilters.getAmountLimit(item);
    }
}
