package com.chain.autostoragesystem.api.storage_system;

import com.chain.autostoragesystem.utils.minecraft.TimeUtil;
import lombok.Getter;

@Getter
public class Config {
    private final int maxItemsPerOperation;
    private final int pauseIntervalSeconds; // pause time between operations

    public Config(int maxItemsPerOperation, int pauseIntervalSeconds) {
        this.maxItemsPerOperation = maxItemsPerOperation;
        this.pauseIntervalSeconds = pauseIntervalSeconds;
    }

    public static Config getDefault() {
        return new Config(1, 1);
    }

    public int getPauseIntervalTicks() {
        return TimeUtil.secondsToTicks(getPauseIntervalSeconds());
    }
}
