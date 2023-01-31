package com.chain.autostoragesystem.block.custom;

import com.chain.autostoragesystem.api.IImportBus;
import com.chain.autostoragesystem.utils.ChatUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Блок, управляющий системой
 */
// Операция = попытка обработки одного запроса импорта И одного запроса экспорта
// Запрос импорта, экспорта = процесс перемещения N-го количества элементов одного типа из одного инвентаря в другой

// Объем операции = максимальное количество элементов одного типа во время выполнения одной операции. Измеряется в int
// Скорость = количество операций за одну секунду. Измеряется в int

//перемещение предметов между storages
/*
Может быть запрос "забрать предметы" - из importStorages (например, из сундука или готовые слитки из печки, готовый предмет из автоверстока).
Откуда - из importStorages.
Куда - в exportStorages или в storages. (Приоритет у exportStorages выше)

Может быть запрос "положить предметы" - в exportStorages (например, в сундук, на плавку в печку, в автоверстак)
Куда - в exportStorages.
Откуда - из importStorages или из storages. (Приоритет у importStorages выше)
 */
//todo должен быть один на всю систему
public class SystemController extends Block {
    List<Object> storages; // can import or export to storage. Items visible in SystemMaster menu.

    Set<IImportBus> importBuses = new HashSet<>(); // can only import from these storages. Items don't visible in SystemMaster menu.
    List<Object> exportStorages; // can only export to these storages. Items don't visible in SystemMaster menu.

    int operationCapacity = 1;
    int operationSpeed = 1;

    public SystemController(Properties properties) {
        super(properties);
    }


    // положить предмет из одного сундука в другой
    private void doImport() {


        //1. узнать, требуется ли положить куда-то предмет. Узнать количество требуемых элементов.
        // (Все что есть, N-е количество и т.д. Зависит как от настроек кабеля, так и от вместимости инвенторя, т.е. количества свободных слотов)
        //todo blacklist, whitelist фильтры, без фильтров. Приоритет загрузки в инвентарь.

        //2. если да, то найти хранилище, где этот предмет есть.

        //3. если такие хранилища найдены, то забрать из хранилищ
        // "количество, не больше чем operationCapacity И не больше, чем требуется"

        //4. положить забранные предметы в инвентарь из шага 1.
    }

    public void addImportBus(Player player, IImportBus importBus) {
        importBuses.add(importBus);
        System.out.println("Add!");
        StringBuilder b = new StringBuilder();
        importBus.getImportRequests().forEach(b::append);
        ChatUtil.playerSendMessage(player, b.toString());
    }


}
