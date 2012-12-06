package org.geeckodev.formadroid.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Day implements Iterable<Lesson> {
	private int id;
	private List<Lesson> lessons;
	private String name;

	public Day(int id, String name) {
		this.id = id;
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

	public int getId() {
		return this.id;
	}

	public Lesson getLesson(int i) {
		return this.lessons.get(i);
	}

	public String getName() {
		return this.name;
	}

	public int size() {
		return this.lessons.size();
	}

	public void clear() {
		this.lessons.clear();
	}

	public void print() {
		System.out.println(">>> " + this.name);

		for (Lesson i : this.lessons) {
			i.print();
		}
	}
}
