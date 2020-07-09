package io.cheukyin699.wiicontroller;

import android.hardware.Sensor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Observable;
import java.util.Observer;

public class ControllerFragment extends Fragment implements Observer {

    private TextView logText;
    private SensorController controller;

    public ControllerFragment(SensorController controller) {
        this.controller = controller;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.controller_fragment, container, false);

        logText = (TextView) view.findViewById(R.id.logText);
        controller.addObserver(this);

        return view;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof SensorController) {
            final SensorController controller = (SensorController) observable;
            final float[] values = controller.getValues();
            StringBuilder builder = new StringBuilder();
            // There will be an extra comma at the end, but we actually just don't care
            for (float f : values) {
                builder.append(f);
                builder.append("\n");
            }

            logText.setText(builder.toString());
        }
    }
}
