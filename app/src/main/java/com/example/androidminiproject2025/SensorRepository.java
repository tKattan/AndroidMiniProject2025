package com.example.androidminiproject2025;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SensorRepository {


    private final Context context;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public SensorRepository(Context context) {
        this.context = context;
    }

    private static boolean correctInput = false;
    private static float accelXEtalon;
    private static float accelYEtalon;
    private static float accelZEtalon;
    private static boolean etalonAccelDone = false;


    public void checkIfMovementIsGood(String difficulty, RepositoryCallback<Boolean> callback, CancellationToken cancellationToken) {
        executorService.execute(() -> {
            SensorManager sensorMan;
            sensorMan = (SensorManager) context.getSystemService(SENSOR_SERVICE);
            SensorEventListener sensorEventListener = getSensorEventListener();
            try {
                PackageManager manager = context.getPackageManager();
                Sensor accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorMan.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);

                while (!cancellationToken.isCancelled() || !correctInput) {

                }
                callback.onComplete(new Result.Success<>(true));
            } finally {
                sensorMan.unregisterListener(sensorEventListener);
            }
        });
    }


    @NonNull
    private static SensorEventListener getSensorEventListener() {
        SensorEventListener sensorEventListener = null;
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    correctInput = computeAccelerometerValues(event.values);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        return sensorEventListener;
    }

    private static boolean computeAccelerometerValues(float[] values){
        if(etalonAccelDone){
            if(accelXEtalon -5 > values[0] ||accelXEtalon +5 < values[0]){
                return true;
            }
            if(accelYEtalon -5 > values[1] ||accelYEtalon +5 < values[1]){
                return true;
            }
            if(accelZEtalon -5 > values[2] ||accelZEtalon +5 < values[2]){
                return true;
            }
            return false;
        }else{
            accelXEtalon = values[0];
            accelYEtalon = values[1];
            accelZEtalon = values[2];
            etalonAccelDone = true;
            return false;
        }
    }

}
