package io.cheukyin699.wiicontroller;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Observable;

public class SensorController extends Observable implements SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor accelerationSensor;
    private final Sensor magneticSensor;
    private float[] currentAcceleration;
    private float[] currentMagnets;
    private float[] currentOrientation;

    SensorController(Activity activity) {
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        currentAcceleration = new float[3];
        currentMagnets = new float[3];
        currentOrientation = new float[3];

        if (accelerationSensor == null) {
            Log.e("SensorInit", "Accelerometer sensor not found; please install on another phone.");
        }
        if (magneticSensor == null) {
            Log.e("SensorInit", "Magnetic field sensor not found; please install on another phone.");
        }
    }

    void register() {
        if (accelerationSensor != null && magneticSensor != null) {
            sensorManager.registerListener(this, accelerationSensor, SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
        } else {
            Log.e("SensorRegister", "Cannot register listeners because sensors don't exist.");
        }
    }

    void unregister() {
        sensorManager.unregisterListener(this);
    }

    private void updateOrientation() {
        float[] R = new float[9];
        float[] I = new float[9];
        boolean gotten = SensorManager.getRotationMatrix(R, I, currentAcceleration, currentMagnets);
        if (gotten) {
            SensorManager.getOrientation(R, currentOrientation);
        } else {
            Log.i("SensorOrientation", "Could not get rotation matrix.");
        }
    }

    public float[] getValues() {
        float[] r = new float[6];

        // Update orientation information in case it is out of date
        updateOrientation();

        r[0] = currentAcceleration[0];
        r[1] = currentAcceleration[1];
        r[2] = currentAcceleration[2];
        r[3] = currentOrientation[0];
        r[4] = currentOrientation[1];
        r[5] = currentOrientation[2];

        return r;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.equals(accelerationSensor)) {
            currentAcceleration = sensorEvent.values;
            setChanged();
            notifyObservers();
        } else if (sensorEvent.sensor.equals(magneticSensor)) {
            currentMagnets = sensorEvent.values;
            setChanged();
            notifyObservers();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
