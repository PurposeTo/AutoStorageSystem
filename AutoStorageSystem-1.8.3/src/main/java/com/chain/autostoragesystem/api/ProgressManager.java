package com.chain.autostoragesystem.api;

public class ProgressManager {

    private int current = 0;
    private final int total;

    private Runnable action;

    public ProgressManager(int total, Runnable action) {
        assert total > current;
        this.total = total;
        this.action = action;
    }

    public void increase() {
        current++;

        if (current >= total) {
            action.run();
            resetProgress();
        }
    }

    private void resetProgress() {
        current = 0;
    }
}
