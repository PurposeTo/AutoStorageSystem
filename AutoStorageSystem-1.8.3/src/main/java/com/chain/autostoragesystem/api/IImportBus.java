package com.chain.autostoragesystem.api;

import net.minecraftforge.items.IItemHandler;

import java.util.Set;

public interface IImportBus {

    /**
     * Получить список подключенных инвентарей, с учетом установленных фильтров
     */
    Set<IItemHandler> getConnectedInventories();

    /**
     * Получить список запросов на изъятие предметов, с учетом установленных фильтров
     */
    Set<ImportRequest> getImportRequests();
}
