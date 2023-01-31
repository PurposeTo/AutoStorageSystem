package com.chain.autostoragesystem.api;

import java.util.Set;

public interface IImportBus {

    /**
     * Получить список запросов на изъятие предметов, с учетом установленных фильтров
     */
    Set<ImportRequest> getImportRequests();
}
