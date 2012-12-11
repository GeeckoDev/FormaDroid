package org.geeckodev.formadroid.activities;

import java.io.IOException;
import java.util.List;

import org.geeckodev.formadroid.R;
import org.geeckodev.formadroid.application.FormaDroid;
import org.geeckodev.formadroid.model.Department;
import org.geeckodev.formadroid.model.Establishment;
import org.geeckodev.formadroid.model.Group;
import org.geeckodev.formadroid.model.Model;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Preference extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {
	private FormaDroid fd;

	private void setEntries(CharSequence pref, CharSequence[] entries, CharSequence[] values) {
		ListPreference lp = (ListPreference) findPreference(pref);
		lp.setEntries(entries);
		lp.setEntryValues(values);
	}

	public void loadSettings() {
		if (!PreferenceManager.getDefaultSharedPreferences(this).getString("estts_pref", "none").equals("none")) {
			fd.model.selectEstablishment(PreferenceManager.getDefaultSharedPreferences(this).getString(
					"estts_pref", "0"));
			new SyncDeptsTask().execute(fd.model);
			((ListPreference) findPreference("estts_pref")).setValue(PreferenceManager
					.getDefaultSharedPreferences(this).getString("estts_pref", "0"));

			if (!PreferenceManager.getDefaultSharedPreferences(this).getString("depts_pref", "none")
					.equals("none")) {
				fd.model.selectDepartment(PreferenceManager.getDefaultSharedPreferences(this).getString(
						"depts_pref", "0"));
				new SyncGroupsTask().execute(fd.model);
				((ListPreference) findPreference("depts_pref")).setValue(PreferenceManager
						.getDefaultSharedPreferences(this).getString("depts_pref", "0"));
				if (PreferenceManager.getDefaultSharedPreferences(this).getString("groups_pref", "none")
						.equals("none"))
					Toast.makeText(this, "Aucun groupe n'a été défini", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fd = (FormaDroid) this.getApplication();
		/* Inflate from XML */
		this.addPreferencesFromResource(R.xml.preferences);

		/* Try to fetch the establishment list */
		new SyncEsttsTask().execute(fd.model);

		/* Loading preferences already existing */
		this.loadSettings();

		/* Preference change listener */
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

		if (Build.VERSION.SDK_INT >= 14.0) {
			ActionBar a = getActionBar();
			a.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
		}

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
		if (key.contains("estts_pref")) {
			((ListPreference) findPreference("depts_pref")).setEnabled(false);
			((ListPreference) findPreference("groups_pref")).setEnabled(false);
			fd.model.selectEstablishment(sp.getString("estts_pref", "0"));
			new SyncDeptsTask().execute(fd.model);
		} else if (key.contains("depts_pref")) {
			((ListPreference) findPreference("groups_pref")).setEnabled(false);
			fd.model.selectDepartment(sp.getString("depts_pref", "0"));
			new SyncGroupsTask().execute(fd.model);
		} else if (key.contains("groups_pref")) {
			fd.model.selectGroup(sp.getString("groups_pref", "0"));
			/* Return to previous Activity */
			this.finish();
		}
	}

	private class SyncEsttsTask extends AsyncTask<Model, Void, Integer> {
		@Override
		protected Integer doInBackground(Model... model) {
			try {
				model[0].buildEstablishments();
			} catch (IOException e) {
				return -1;
			}

			return 0;
		}

		protected void onPostExecute(Integer result) {
			if (result != 0) {
				Toast.makeText(Preference.this, "Impossible de récupérer la liste des établissements",
						Toast.LENGTH_SHORT).show();
				return;
			}

			List<Establishment> estts = fd.model.getEstablishments();
			String[] entries = new String[estts.size()];
			String[] values = new String[estts.size()];

			int i = 0;
			for (Establishment estt : estts) {
				entries[i] = estt.getName();
				values[i] = estt.getValue();
				i++;
			}

			Preference.this.setEntries("estts_pref", entries, values);
			((ListPreference) findPreference("estts_pref")).setEnabled(true);
		}
	}

	private class SyncDeptsTask extends AsyncTask<Model, Void, Integer> {
		@Override
		protected Integer doInBackground(Model... model) {
			try {
				model[0].buildDepartments();
			} catch (IOException e) {
				return -1;
			}

			return 0;
		}

		protected void onPostExecute(Integer result) {
			if (result != 0) {
				Toast.makeText(Preference.this, "Impossible de récupérer la liste des départements",
						Toast.LENGTH_SHORT).show();
				return;
			}

			List<Department> depts = fd.model.getDepartments();
			String[] entries = new String[depts.size()];
			String[] values = new String[depts.size()];

			int i = 0;
			for (Department dept : depts) {
				entries[i] = dept.getName();
				values[i] = dept.getValue();
				i++;
			}

			Preference.this.setEntries("depts_pref", entries, values);
			((ListPreference) findPreference("depts_pref")).setEnabled(true);
		}
	}

	private class SyncGroupsTask extends AsyncTask<Model, Void, Integer> {
		@Override
		protected Integer doInBackground(Model... model) {
			try {
				model[0].buildGroups();
			} catch (IOException e) {
				return -1;
			}

			return 0;
		}

		protected void onPostExecute(Integer result) {
			if (result != 0) {
				Toast.makeText(Preference.this, "Impossible de récupérer la liste des groupes",
						Toast.LENGTH_SHORT).show();
				return;
			}

			List<Group> groups = fd.model.getGroups();
			String[] entries = new String[groups.size()];
			String[] values = new String[groups.size()];

			int i = 0;
			for (Group group : groups) {
				entries[i] = group.getName();
				values[i] = group.getValue();
				i++;
			}

			Preference.this.setEntries("groups_pref", entries, values);

			((ListPreference) findPreference("groups_pref")).setEnabled(true);
		}
	}
}