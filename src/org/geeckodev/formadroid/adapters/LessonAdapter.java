package org.geeckodev.formadroid.adapters;

import java.util.Calendar;

import org.geeckodev.formadroid.R;
import org.geeckodev.formadroid.application.FormaDroid;
import org.geeckodev.formadroid.model.Day;
import org.geeckodev.formadroid.model.Lesson;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LessonAdapter extends BaseAdapter {
	FormaDroid fd;
	Day day;
	LayoutInflater inflater;

	public LessonAdapter(Context context, Day day) {
		this.fd = (FormaDroid) context.getApplicationContext();
		this.day = day;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return this.day.size();
	}

	@Override
	public Object getItem(int i) {
		return this.day.getLesson(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	private class ViewHolder {
		TextView tvSubgroup;
		TextView tvName;
		TextView tvBegin;
		TextView tvEnd;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.itemlesson, null);
			holder.tvSubgroup = (TextView) convertView
					.findViewById(R.id.tvSubgroup);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvBegin = (TextView) convertView.findViewById(R.id.tvBegin);
			holder.tvEnd = (TextView) convertView.findViewById(R.id.tvEnd);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Lesson lesson = this.day.getLesson(i);
		int curr_day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

		/* Change the color depending on the current day and hour */

		if (this.day.getNumber() == curr_day) {
			if (lesson.isFinished())
				holder.tvName.setTextColor(Color.GRAY);
			else if (lesson.isOngoing()) {
				holder.tvName.setTextColor(Color.BLUE);
			}
		}

		/* Set text */

		if (lesson.getSubgroup() != "") {
			holder.tvSubgroup.setTypeface(null, Typeface.ITALIC);
			holder.tvSubgroup.setText(lesson.getSubgroup() + "  ");
		}
		holder.tvName.setText(lesson.getName());
		holder.tvBegin.setText(lesson.getBegin());
		holder.tvEnd.setText(lesson.getEnd());

		return convertView;
	}
}
