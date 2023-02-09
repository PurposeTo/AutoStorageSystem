package com.chain.autostoragesystem.api.wrappers.container;

import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ContainerWrapper {

    @NotNull
    private final Container container;

    public ContainerWrapper(@NotNull Container container) {
        this.container = container;
    }


    @Nonnull
    public Set<Item> getItemTypes() {
        return new HashSet<>(getNotEmptyStacks()
                .stream()
                .collect(Collectors.groupingBy(ItemStack::getItem))
                .keySet());
    }

    @NotNull
    public List<ItemStack> getNotEmptyStacks() {
        return getStacks().stream()
                .filter(it -> !it.isEmpty())
                .collect(Collectors.toList());
    }

    @NotNull
    public List<ItemStack> getStacks() {
        int slots = this.getContainerSize();
        List<ItemStack> result = new ArrayList<>();
        for (int slot = 0; slot < slots; slot++) {
            ItemStack stack = container.getItem(slot);
            result.add(stack);
        }
        return result;
    }

    private int getContainerSize() {
        return this.container.getContainerSize();
    }
}
