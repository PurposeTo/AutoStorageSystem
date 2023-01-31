package com.chain.autostoragesystem.utils.common;

import java.util.concurrent.ThreadLocalRandom;

public class Random {

    public static int range(int min, int max) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
