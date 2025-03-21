package com.example.androidminiproject2025;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timber.log.Timber;

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
            correctInput = false;
            SensorManager sensorMan;
            sensorMan = (SensorManager) context.getSystemService(SENSOR_SERVICE);
            SensorEventListener sensorEventListener = getSensorEventListener();
            try {
                Sensor accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorMan.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
                Timber.d("Sensor registered");
                while (!cancellationToken.isCancelled() && !correctInput) {
                    //noinspection BusyWait
                    Thread.sleep(100);
                }
                callback.onComplete(new Result.Success<>(true));
            } catch (Exception e) {
                Timber.e(e, "erreur binding sensor accelerometer");
                callback.onComplete(new Result.Error<>(e));
            }finally {
                sensorMan.unregisterListener(sensorEventListener);
            }
        });
    }

    public void checkIfMicrophoneInputIsGood(String difficulty, RepositoryCallback<Boolean> callback, CancellationToken cancellationToken) {
        executorService.execute(() -> {
            correctInput = false;
            MediaRecorder mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile("/dev/null");  // Discard recorded audio
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try{
                mediaRecorder.prepare();
                mediaRecorder.start();
                while (!cancellationToken.isCancelled() && !updateDecibelLevel(mediaRecorder)) {
                    //noinspection BusyWait
                    Thread.sleep(200);
                }
                callback.onComplete(new Result.Success<>(true));
            }catch (Exception e){
                Timber.e(e, "erreur binding audio recorder");
                callback.onComplete(new Result.Error<>(e));
            } finally {
                mediaRecorder.release();
            }
        });
    }

    private boolean updateDecibelLevel(MediaRecorder mediaRecorder) {
        if(mediaRecorder != null){
            double amplitude = mediaRecorder.getMaxAmplitude();
            System.out.println(amplitude);
            double decibels = 20 * Math.log10(amplitude == 0 ? 1 : amplitude);

            if(decibels >= 85.0){
                return true;
            }
            Timber.i( "Sound Level: %.2f dB",  decibels);
            return false;
        }
        return false;
    }


    @NonNull
    private static SensorEventListener getSensorEventListener() {
        SensorEventListener sensorEventListener = null;
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    Timber.d("Accelerometer values: %f, %f, %f", event.values[0], event.values[1], event.values[2]);
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
