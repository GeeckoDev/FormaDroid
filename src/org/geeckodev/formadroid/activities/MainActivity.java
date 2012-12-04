package org.geeckodev.formadroid.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.geeckodev.formadroid.R;
import org.geeckodev.formadroid.adapters.DaysPagerAdapter;
import org.geeckodev.formadroid.fragments.DayFragment;
import org.geeckodev.formadroid.model.Model;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private Model model;
	private Spinner sGroup;
	private Button btnRefresh;
	private ViewPager vpDays;
	private DaysPagerAdapter paDays;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /* Construct the model */
        
        this.model = new Model();
        
		/* Construct the view */
		
        setContentView(R.layout.activity_main);
        this.sGroup = (Spinner)findViewById(R.id.sGroup);
        this.btnRefresh = (Button)findViewById(R.id.btnRefresh);
		this.vpDays = (ViewPager)findViewById(R.id.vpDays);
        
        // Spinner
    	List<String> list = new ArrayList<String>();
    	for (String i : this.model.getGroups()) {
    		list.add(i);
    	}
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
    			this, android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	this.sGroup.setAdapter(dataAdapter);
        
    	this.sGroup.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				model.selectGroup(model.getGroups()[pos]);	
				new SynchronizeTask().execute(model);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}    		
    	});
    	
    	// Button
        this.btnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new SynchronizeTask().execute(model);
			}
        });
        
        // ViewPager
        this.paDays = new DaysPagerAdapter(super.getSupportFragmentManager());
		this.vpDays.setAdapter(this.paDays);
		
		for (int i=0; i<5; i++) {
			this.paDays.addItem(Fragment.instantiate(this, DayFragment.class.getName()));
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void update() {
    	int j=0;
    	for (Fragment i : this.paDays) {
    		((DayFragment)i).update(this.model.getCurrentDay(j));
    		j++;
    	}
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
	    		Toast.makeText(ctx, "Erreur de synchronisation", Toast.LENGTH_SHORT).show();
	    		return;
	    	}
	    	
	    	update();
	    	Toast.makeText(ctx, "Synchronisation terminée", Toast.LENGTH_SHORT).show();
	    }
	}
}
