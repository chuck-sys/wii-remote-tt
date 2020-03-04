package io.cheukyin699.wiicontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SensorController sensorController;
    private DataSender dataSender;
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.registerOnSharedPreferenceChangeListener(this);

        sensorController = new SensorController(this);
        startBtn = findViewById(R.id.startBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorController.register();
            }
        });

        initDataSender(prefs);
        sensorController.addObserver(dataSender);
    }

    private void initDataSender(final SharedPreferences prefs) {
        dataSender = new DataSender(
                prefs.getString("IP", "127.0.0.1"),
                prefs.getInt("PORT", 8080)
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorController.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorController.unregister();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        initDataSender(sharedPreferences);
    }
}
