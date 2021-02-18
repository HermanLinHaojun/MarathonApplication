package com.example.marathonapplication.deploy;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.marathonapplication.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}