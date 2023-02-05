package com.chain.autostoragesystem.utils.minecraft;

public class TimeUtil {

    public static final int TICKS_PER_SECOND = 20;
    public static final double DELTA_SEC = 1d / TICKS_PER_SECOND;

    public static int secondsToTicks(double seconds) {
        return (int) Math.max(Math.round(seconds * TimeUtil.TICKS_PER_SECOND), 1);
    }
}
