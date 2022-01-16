package io.cheukyin699.wiicontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final String REGISTRATION_TAG = "registration";

    private ControllerPreferencesFragment preferences = new ControllerPreferencesFragment();
    private SensorController sensorController;
    private DataSender dataSender;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private boolean isPreferencesOpen() {
        return getSupportFragmentManager().findFragmentById(preferences.getId()) != null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final FragmentManager fm = getSupportFragmentManager();
        if (item.getItemId() == R.id.preferencesItem) {
            if (!isPreferencesOpen()) {
                fm.beginTransaction()
                        .replace(R.id.main_frame, preferences)
                        .addToBackStack(null)
                        .commit();
            } else {
                fm.popBackStack();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorController = new SensorController(this);
        sensorController.unregister();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.registerOnSharedPreferenceChangeListener(this);

        initDataSender(prefs);
        sensorController.addObserver(dataSender);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_frame, new RegistrationFragment(sensorController), REGISTRATION_TAG)
                .commit();
    }

    private void initDataSender(final SharedPreferences prefs) {
        dataSender = new DataSender(
                prefs.getString("serverAddress", "127.0.0.1"),
                Integer.parseInt(prefs.getString("serverPort", "8080"))
        );
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
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
