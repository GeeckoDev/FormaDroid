package org.geeckodev.formadroid.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.geeckodev.formadroid.R;
import org.geeckodev.formadroid.adapters.DaysPagerAdapter;
import org.geeckodev.formadroid.application.FormaDroid;
import org.geeckodev.formadroid.fragments.DayFragment;
import org.geeckodev.formadroid.model.Model;
import org.geeckodev.formadroid.preferences.Preference;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private static int PAGE_NBR = 5;

	private FormaDroid fd;
	private Spinner sGroup;
	private Button btnRefresh;
	private ViewPager vpDays;
	private DaysPagerAdapter paDays;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fd = (FormaDroid) this.getApplication();

		/* Construct the view */

		setContentView(R.layout.activity_main);
		this.sGroup = (Spinner) findViewById(R.id.sGroup);
		this.btnRefresh = (Button) findViewById(R.id.btnRefresh);
		this.vpDays = (ViewPager) findViewById(R.id.vpDays);

		// Spinner
		List<String> list = new ArrayList<String>();
		for (String i : fd.model.getGroups()) {
			list.add(i);
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.sGroup.setAdapter(dataAdapter);
		
		this.sGroup.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				fd.model.selectGroup(fd.model.getGroups()[pos]);
				new SynchronizeTask().execute(fd.model);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		this.sGroup.setSelection(Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("groups_pref", "0")));

		// Button
		this.btnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new SynchronizeTask().execute(fd.model);
			}
		});

		// ViewPager
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_search)
			startActivity(new Intent(MainActivity.this, Preference.class));
		return true;
	}

	public void update() {
		this.paDays.update();
	}

	private class SynchronizeTask extends AsyncTask<Model, Void, Integer> {

		@Override
		protected Integer doInBackground(Model... model) {
			try {
				model[0].build();
			} catch (IOException e) {
				return -1;
			}

			return 0;
		}

		protected void onPostExecute(Integer result) {
			Context ctx = getApplicationContext();

			if (result != 0) {
				Toast.makeText(ctx, "Erreur de synchronisation",
						Toast.LENGTH_SHORT).show();
				return;
			}

			update();
			Toast.makeText(ctx, "Synchronisation terminée", Toast.LENGTH_SHORT)
					.show();
		}
	}
}
