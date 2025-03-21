package com.example.androidminiproject2025;

public class CancellationToken {
    private volatile boolean cancelled = false;

    public void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
