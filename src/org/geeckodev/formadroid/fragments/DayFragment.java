package org.geeckodev.formadroid.fragments;

import org.geeckodev.formadroid.R;
import org.geeckodev.formadroid.adapters.LessonAdapter;
import org.geeckodev.formadroid.application.FormaDroid;
import org.geeckodev.formadroid.model.Day;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class DayFragment extends Fragment {
	private FormaDroid fd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fd = (FormaDroid) this.getActivity().getApplication();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_day, container, false);
		update(view);

		return view;
	}

	public void update() {
		View view = getView();
		/* outside the screen, abort */
		if (view == null) {
			return;
		}

		update(view);
	}

	private void update(View view) {
		/* Model is being updated, abort */
		if (fd.model.isPending())
			return;

		int pos = this.getArguments().getInt("pos");
		Day day = fd.model.getCurrentDay(pos);

		/* Day is undefined, keep the default view and abort */
		if (day == null)
			return;

		ListView lvLesson = (ListView) view.findViewById(R.id.lvLesson);
		TextView tvToday = (TextView) view.findViewById(R.id.tvToday);

		tvToday.setText("Cours du " + day.getName());
		
		LessonAdapter lessonAdapter = new LessonAdapter(getActivity(), day);
		lvLesson.setAdapter(lessonAdapter);
	}
}
