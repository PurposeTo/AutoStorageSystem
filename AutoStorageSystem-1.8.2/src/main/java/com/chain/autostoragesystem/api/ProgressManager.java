package com.chain.autostoragesystem.api;

public class ProgressManager {

    private int current = 0;
    private int delayTicks;

    private final Runnable action;

    public ProgressManager(int delayTicks, Runnable action) {
        assert delayTicks > 0;
        this.delayTicks = delayTicks;
        this.action = action;
    }

    public void tick() {
        current++;
        checkState();
    }

    public void setDelay(int delayTicks) {
        assert delayTicks > 0;
        this.delayTicks = delayTicks;
        checkState();
    }

    private void checkState() {
        if (current >= delayTicks) {
            action.run();
            resetProgress();
        }
    }

    private void resetProgress() {
        current = 0;
    }
}
