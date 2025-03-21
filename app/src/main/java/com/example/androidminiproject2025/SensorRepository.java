package com.example.androidminiproject2025;

public class SensorRepository {
    public SensorRepository() {

    }

//    private Result<Boolean> checkIfMovementIsGood() {
//        return new Result.Success<>(sensors.getAccelerometer().getZ() > 0.5);
//    }

    public void checkIfMovementIsGood(String x, RepositoryCallback<Boolean> callback, CancellationToken cancellationToken) {
        executor.
        try {
            // While
            boolean isGood = checkIfMovementIsGood();
            // log
            callback.onComplete(new Result.Success<>(isGood));
        }finally {
            // unregister
        }
    }

}
