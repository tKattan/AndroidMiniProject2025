package com.example.androidminiproject2025;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SensorRepository {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public SensorRepository() {
    }

    public void checkIfMovementIsGood(String x, RepositoryCallback<Boolean> callback, CancellationToken cancellationToken) {
        executorService.submit(() -> {
            while (!cancellationToken.isCancelled()) {
                // Simulate checking movement
                boolean isGoodMovement = checkMovement();
                if (isGoodMovement){
                    callback.onComplete(new Result.Success<>(true));
                    return;
                }
            }
        });
    }

    private boolean checkMovement() {
        // Simulate movement check logic
        return Math.random() > 0.5;
    }
}
