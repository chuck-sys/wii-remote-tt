package io.cheukyin699.wiicontroller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class RegistrationFragment extends Fragment {

    private SensorController controller;

    public RegistrationFragment(SensorController controller) {
        this.controller = controller;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);

        Button startBtn = view.findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new ControllerFragment(controller))
                        .addToBackStack(null)
                        .commit();

                controller.register();
            }
        });

        return view;
    }
}
