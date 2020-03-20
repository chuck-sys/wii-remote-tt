package io.cheukyin699.wiicontroller;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class ControllerPreferencesFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
