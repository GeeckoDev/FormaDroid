package org.geeckodev.formadroid.preferences;

import org.geeckodev.formadroid.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preference extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.addPreferencesFromResource(R.xml.preferences);
		
	}
}
