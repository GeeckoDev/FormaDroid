package org.geeckodev.formadroid.activities;

import org.geeckodev.formadroid.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preference extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {
	private void setEntries(CharSequence pref, CharSequence[] entries,
			CharSequence[] values) {
		ListPreference lp = (ListPreference) findPreference(pref);
		lp.setEntries(entries);
		lp.setEntryValues(values);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Indicate that the preferences were opened at least once */

		SharedPreferences prefs = getSharedPreferences("MyPreferences",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.putBoolean("HaveShownPrefs", true);
		ed.commit();

		/* Inflate from XML and dynamically create the attributes */

		this.addPreferencesFromResource(R.xml.preferences);
		this.setEntries("estts_pref", new CharSequence[] {},
				new CharSequence[] {});
		this.setEntries("depts_pref", new CharSequence[] {},
				new CharSequence[] {});
		this.setEntries("groups_pref", new CharSequence[] {},
				new CharSequence[] {});

		/* Preference change listener */

		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sp, String key) {

	}
}