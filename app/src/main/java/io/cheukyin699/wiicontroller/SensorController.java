package io.cheukyin699.wiicontroller;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorController implements SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor gravitySensor;
    private final Sensor magneticSensor;
    private float[] currentGravity;
    private float[] currentMagnets;
    private float[] currentOrientation;

    SensorController(Activity activity) {
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        currentGravity = new float[3];
        currentMagnets = new float[3];
        currentOrientation = new float[3];

        if (gravitySensor == null) {
            Log.e("SensorInit", "Gravity sensor not found; please install on another phone.");
        }
    }

    void register() {
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    void unregister() {
        sensorManager.unregisterListener(this);
    }

    public float[] getOrientation() {
        float[] R = new float[9];
        float[] I = new float[9];
        boolean gotten = SensorManager.getRotationMatrix(R, I, currentGravity, currentMagnets);
        if (gotten) {
            SensorManager.getOrientation(R, currentOrientation);
            return currentOrientation;
        } else {
            Log.i("SensorOrientation", "Could not get rotation matrix; using old orientation");
            return currentOrientation;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.equals(gravitySensor)) {
            currentGravity = sensorEvent.values;
        } else if (sensorEvent.sensor.equals(magneticSensor)) {
            currentMagnets = sensorEvent.values;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
