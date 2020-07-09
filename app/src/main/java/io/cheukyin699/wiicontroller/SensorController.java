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
    private final Sensor rotationSensor;
    private float[] currentAcceleration;
    private float[] currentRotation;

    SensorController(Activity activity) {
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        currentAcceleration = new float[3];
        currentRotation = new float[5];

        if (accelerationSensor == null) {
            Log.e("SensorInit", "Accelerometer sensor not found; please install on another phone.");
        }
        if (rotationSensor == null) {
            Log.e("SensorInit", "Rotation vector sensor not found; please install on another phone.");
        }
    }

    void register() {
        if (accelerationSensor != null && rotationSensor != null) {
            sensorManager.registerListener(this, accelerationSensor, SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_GAME);
        } else {
            Log.e("SensorRegister", "Cannot register listeners because sensors don't exist.");
        }
    }

    void unregister() {
        sensorManager.unregisterListener(this);
    }

    public float[] getValues() {
        float[] r = new float[8];

        r[0] = currentAcceleration[0];
        r[1] = currentAcceleration[1];
        r[2] = currentAcceleration[2];
        r[3] = currentRotation[0];
        r[4] = currentRotation[1];
        r[5] = currentRotation[2];
        r[6] = currentRotation[3];
        r[7] = currentRotation[4];

        return r;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.equals(accelerationSensor)) {
            currentAcceleration = sensorEvent.values;
            setChanged();
            notifyObservers();
        } else if (sensorEvent.sensor.equals(rotationSensor)) {
            currentRotation = sensorEvent.values;
            setChanged();
            notifyObservers();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
