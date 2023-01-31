package com.chain.autostoragesystem.api;

public class ProgressManager {

    private int current = 0;
    private int total;

    private final Runnable action;

    public ProgressManager(int total, Runnable action) {
        assert total > current;
        this.total = total;
        this.action = action;
    }

    public void increase() {
        current++;
        checkState();
    }

    public void setTotal(int total) {
        assert total > current;
        this.total = total;
        checkState();
    }

    private void checkState() {
        if (current >= total) {
            action.run();
            resetProgress();
        }
    }

    private void resetProgress() {
        current = 0;
    }
}
