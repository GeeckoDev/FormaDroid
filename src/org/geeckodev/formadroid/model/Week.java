package org.geeckodev.formadroid.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Week implements Iterable<Day> {
	private List<Day> days;
	
	public Week() {
		this.days = new ArrayList<Day>();
		this.days.add(new Day("Lundi"));
		this.days.add(new Day("Mardi"));
		this.days.add(new Day("Mercredi"));
		this.days.add(new Day("Jeudi"));
		this.days.add(new Day("Vendredi"));
		this.days.add(new Day("Samedi"));
		this.days.add(new Day("Dimanche"));
	}

	@Override
	public Iterator<Day> iterator() {
		return this.days.iterator();
	}
	
	public Day getDay(int i) {
		return this.days.get(i);
	}
	
	public Day getDay(String name) {
		for (Day i : this.days) {
			if (i.getName() == name) {
				return i;
			}
		}
		
		return null;
	}
	
	public int size() {
		return this.days.size();
	}
	
	public void clear() {
		for (Day i : this.days) {
			i.clear();
		}
	}
	
	public void print() {
		for (Day i : this.days) {
			i.print();
		}
	}
}
