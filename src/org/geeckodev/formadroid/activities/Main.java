package org.geeckodev.formadroid.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.geeckodev.formadroid.R;
import org.geeckodev.formadroid.adapters.DaysPagerAdapter;
import org.geeckodev.formadroid.application.FormaDroid;
import org.geeckodev.formadroid.fragments.DayFragment;
import org.geeckodev.formadroid.model.Group;
import org.geeckodev.formadroid.model.Model;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Main extends FragmentActivity {
	private static int PAGE_NBR = 5;

	private FormaDroid fd;
	private Spinner sGroup;
	private ViewPager vpDays;
	private DaysPagerAdapter paDays;
	private BroadcastReceiver br = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fd = (FormaDroid) this.getApplication();

		/* Construct the view */

		setContentView(R.layout.activity_main);
		this.sGroup = (Spinner) findViewById(R.id.sGroup);
		this.vpDays = (ViewPager) findViewById(R.id.vpDays);

		/* Create the ViewPager */

		this.paDays = new DaysPagerAdapter(super.getSupportFragmentManager());
		for (int i = 0; i < PAGE_NBR; i++) {
			Bundle b = new Bundle();
			b.putInt("pos", i);

			DayFragment frag = (DayFragment) Fragment.instantiate(this,
					DayFragment.class.getName());
			frag.setArguments(b);
			this.paDays.addItem(frag);

		}
		this.vpDays.setAdapter(this.paDays);

		/* Create the spinner */

		sGroup.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				fd.model.selectGroup(fd.model.getGroups().get(pos).getValue());
				new SyncDaysTask().execute(fd.model);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		/* Update the view every minute */

		this.br = new BroadcastReceiver() {
			@Override
			public void onReceive(Context ctx, Intent intent) {
				if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
					Main.this.paDays.update();
				}
			}
		};

		registerReceiver(this.br, new IntentFilter(Intent.ACTION_TIME_TICK));

		/* Check if it is the first run */

		if (PreferenceManager.getDefaultSharedPreferences(this)
				.getString("groups_pref", "none").equals("none")) {
			startActivity(new Intent(Main.this, Preference.class));
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		/* Try to fetch the group list */

		if (!PreferenceManager.getDefaultSharedPreferences(this)
				.getString("groups_pref", "none").equals("none")) {
			new SyncGroupsTask().execute(fd.model);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_search)
			startActivity(new Intent(Main.this, Preference.class));
		return true;
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
				Toast.makeText(Main.this,
						"Impossible de récupérer la liste des groupes",
						Toast.LENGTH_SHORT).show();
				return;
			}

			/* Update the spinner */

			// Get the group name list
			List<String> list = new ArrayList<String>();
			for (Group i : fd.model.getGroups()) {
				list.add(i.getName());
			}

			// Set the adapter
			ArrayAdapter<String> adapter;
			adapter = new ArrayAdapter<String>(Main.this,
					android.R.layout.simple_spinner_item, list);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sGroup.setAdapter(adapter);

			// Set to the preferred group
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(Main.this);
			String pref = prefs.getString("groups_pref", "0");
			int i = 0;

			for (Group group : fd.model.getGroups()) {
				if (pref.contains(group.getValue())) {
					sGroup.setSelection(i);
				}
				i++;
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		
		if (this.br != null)
			unregisterReceiver(this.br);
	}

	public class SyncDaysTask extends AsyncTask<Model, Void, Integer> {
		@Override
		protected Integer doInBackground(Model... model) {
			try {
				model[0].buildDays();
			} catch (IOException e) {
				return -1;
			}

			return 0;
		}

		protected void onPostExecute(Integer result) {
			if (result != 0) {
				Toast.makeText(Main.this, "Erreur de synchronisation",
						Toast.LENGTH_SHORT).show();
				return;
			}

			paDays.update();
		}
	}
}
