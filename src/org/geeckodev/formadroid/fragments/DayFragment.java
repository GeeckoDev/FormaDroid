package org.geeckodev.formadroid.fragments;

import org.geeckodev.formadroid.R;
import org.geeckodev.formadroid.adapters.LessonAdapter;
import org.geeckodev.formadroid.model.Day;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class DayFragment extends Fragment {
	private ListView lvLesson;
	private TextView tvToday;
	private Day day;  // degueu
	
	public DayFragment() {
		this.day = null; // degueu
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_day, container, false);
		
        this.lvLesson = (ListView)view.findViewById(R.id.lvLesson);
        this.tvToday = (TextView)view.findViewById(R.id.tvToday);
        
        if (this.day != null) {
        	update(this.day); // degueu
        }
        
		return view; 
	}
	
	public void update(Day day) {
		if (day == null)
			return;
		
		this.day = day; // beurk
		
		if (this.lvLesson == null) // franchement degueulasse !!! mais ca marche
			return;
		
	    tvToday.setText("Cours du " + day.getName());
	    
		LessonAdapter lessonAdapter = new LessonAdapter(getActivity(), day);
		lvLesson.setAdapter(lessonAdapter);
	}
}
