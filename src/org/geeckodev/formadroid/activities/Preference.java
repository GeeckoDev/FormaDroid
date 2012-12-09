package org.geeckodev.formadroid.activities;

import org.geeckodev.formadroid.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preference extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.getApplicationContext();
		SharedPreferences prefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.putBoolean("HaveShownPrefs", true);
		ed.commit();

		this.addPreferencesFromResource(R.xml.preferences);
	}
}