package org.geeckodev.formadroid.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Lesson {
	private String begin;
	private String end;
	private String subgroup;
	private String name;

	public Lesson(String begin, String end, String subgroup, String name) {
		this.begin = begin;
		this.end = end;
		this.subgroup = subgroup;
		this.name = name;
	}

	public String getBegin() {
		return this.begin;
	}

	public String getEnd() {
		return this.end;
	}

	public String getSubgroup() {
		return this.subgroup;
	}

	public String getName() {
		return this.name;
	}

	private int[] getCurrentMinuteHour() {
		int[] values = new int[2];

		Date date = new Date();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		values[0] = calendar.get(Calendar.HOUR_OF_DAY);
		values[1] = calendar.get(Calendar.MINUTE);

		return values;
	}

	public boolean isFinished() {
		String[] s = this.end.split(":");
		int hour = Integer.parseInt(s[0]);
		int minute = Integer.parseInt(s[1]);

		int[] values = getCurrentMinuteHour();
		int current_hour = values[0];
		int current_minute = values[1];

		return (current_hour > hour)
				|| (current_hour == hour && current_minute > minute);
	}

	public boolean isOngoing() {
		String[] b = this.begin.split(":");
		int begin_hour = Integer.parseInt(b[0]);
		int begin_minute = Integer.parseInt(b[1]);

		String[] e = this.end.split(":");
		int end_hour = Integer.parseInt(e[0]);
		int end_minute = Integer.parseInt(e[1]);

		int[] values = getCurrentMinuteHour();
		int current_hour = values[0];
		int current_minute = values[1];

		return ((begin_hour < current_hour && current_hour < end_hour)
				|| (begin_hour == current_hour && begin_minute <= current_minute) || (end_hour == current_hour && end_minute >= current_minute));

	}
}
