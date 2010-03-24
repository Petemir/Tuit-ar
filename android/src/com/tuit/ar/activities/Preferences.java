package com.tuit.ar.activities;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;

import com.tuit.ar.R;
import com.tuit.ar.models.Settings;

public class Preferences extends PreferenceActivity {
	ListPreference updateInterval;
	CheckBoxPreference automaticUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);

		automaticUpdate = (CheckBoxPreference) findPreference(Settings.AUTOMATIC_UPDATE);
		automaticUpdate.setPersistent(true);
		automaticUpdate.setDefaultValue(Boolean.TRUE);
		automaticUpdate.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				updateInterval.setEnabled(automaticUpdate.isChecked());
				updateSettings();
				return false;
			}
		});
		updateInterval = (ListPreference) findPreference(Settings.UPDATE_INTERVAL);
		updateInterval.setPersistent(true);
		updateInterval.setDefaultValue("5");
		updateInterval.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				updateSettings();
				return false;
			}
		});
		
	}
	protected void updateSettings() {
		Settings.getInstance().callObservers();
	}
}