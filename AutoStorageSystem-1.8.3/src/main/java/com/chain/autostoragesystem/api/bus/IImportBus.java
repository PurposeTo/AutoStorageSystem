package com.chain.autostoragesystem.api.bus;

import net.minecraft.core.BlockPos;

import java.util.Set;

public interface IImportBus {

    BlockPos getPos();

    /**
     * Получить список запросов на изъятие предметов, с учетом установленных фильтров
     */
    Set<ImportRequest> getImportRequests();
}
