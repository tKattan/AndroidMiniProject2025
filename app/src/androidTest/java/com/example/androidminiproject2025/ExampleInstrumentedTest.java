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
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static boolean correctInput = false;
    private static float accelXEtalon;
    private static float accelYEtalon;
    private static float accelZEtalon;
    private static boolean etalonAccelDone = false;


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.androidminiproject2025", appContext.getPackageName());
    }

    @Test
    public void testGyroscope() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        PackageManager manager = appContext.getPackageManager();
        boolean hasAccelerometer = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        System.out.println("hasAccelerometer = " + hasAccelerometer);
        SensorManager sensorMan = (SensorManager) appContext.getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener sensorEventListener = getSensorEventListener();
        sensorMan.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        while (true){

        }
    }

    @NonNull
    private static SensorEventListener getSensorEventListener() {
        SensorEventListener sensorEventListener = null;
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    System.out.println("X = " +event.values[0]);
                    System.out.println("Y = " +event.values[1]);
                    System.out.println("Z = " +event.values[2]);
                    System.out.println(computeAccelerometerValues(event.values));
                }
                if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                    System.out.println(event.values[0]);
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

    @Test
    public void testBlowing() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (appContext.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + appContext.getPackageName() + " android.permission.RECORD_AUDIO");
            }
        }

        PackageManager manager = appContext.getPackageManager();
        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile("/dev/null");  // Discard recorded audio
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try{
            mediaRecorder.prepare();
            mediaRecorder.start();
            while (!updateDecibelLevel(mediaRecorder)) {
                //noinspection BusyWait
                Thread.sleep(200);
            }
            System.out.println("YYYYEEESSSS");
        }catch (IOException | InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    private boolean updateDecibelLevel(MediaRecorder mediaRecorder) {
        if(mediaRecorder != null){
            double amplitude = mediaRecorder.getMaxAmplitude();
            double decibels = 20 * Math.log10(amplitude == 0 ? 1 : amplitude);
            if(decibels >= 85.0){
                return true;
            }
            System.out.println("Sound Level: " + String.format("%.2f", decibels) + " dB");
            return false;
        }
        return false;
    }
}