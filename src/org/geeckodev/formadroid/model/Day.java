package org.geeckodev.formadroid.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Day implements Iterable<Lesson> {
	private List<Lesson> lessons;
	private String name;

	public Day(String name) {
		this.lessons = new ArrayList<Lesson>();
		this.name = name;
	}

	public void addLesson(Lesson lesson) {
		this.lessons.add(lesson);
	}

	@Override
	public Iterator<Lesson> iterator() {
		return this.lessons.iterator();
	}

	public Lesson getLesson(int i) {
		return this.lessons.get(i);
	}

	public String getName() {
		return this.name;
	}

	public int getNumber() {
		return Integer.parseInt(this.name.split(" ")[1]);
	}
	
	public int size() {
		return this.lessons.size();
	}

	public void clear() {
		this.lessons.clear();
	}
}
