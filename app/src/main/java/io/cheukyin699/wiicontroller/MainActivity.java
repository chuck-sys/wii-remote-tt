package io.cheukyin699.wiicontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final String REGISTRATION_TAG = "registration";

    private RegistrationFragment registrationFragment;

    private SensorController sensorController;
    private DataSender dataSender;

    public MainActivity() {
        sensorController = new SensorController(this);
        registrationFragment = new RegistrationFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferencesItem:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new ControllerPreferencesFragment())
                        .addToBackStack(null)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.registerOnSharedPreferenceChangeListener(this);

        initDataSender(prefs);
        sensorController.addObserver(dataSender);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_frame, registrationFragment, REGISTRATION_TAG)
                .commit();
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
