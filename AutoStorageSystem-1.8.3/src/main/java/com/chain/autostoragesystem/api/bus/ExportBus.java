package com.chain.autostoragesystem.api.bus;

import lombok.Getter;
import net.minecraft.core.BlockPos;

import javax.annotation.Nonnull;

public class ExportBus extends ItemHandlersConnector {

    @Getter
    @Nonnull
    private final BlockPos pos;


    public ExportBus(@Nonnull BlockPos pos) {
        this.pos = pos;
    }

//    // без фильтров: получить слоты, в которые можно положить предмет
//    //todo учитывать фильтры
//    private void getImportRequest(IItemHandler inventory) {
//        List<ImportRequest> requests = new ArrayList<>();
//
//        int slots = inventory.getSlots();
//        for (int slot = 0; slot < slots; slot++) {
//            ItemStack stack = inventory.getStackInSlot(slot);
//
//            if (stack.isEmpty() || !ItemStackUtil.isFull(stack)) {
//                ImportRequest request = new ImportRequest(inventory, stack, slot);
//                requests.add(request);
//            }
//        }
//
//        return requests;
//    }
}
