package org.geeckodev.formadroid.model;

public class Lesson {
	private String begin;
	private String end;
	private String name;
	
	public Lesson(String begin, String end, String name) {
		this.begin = begin;
		this.end = end;
		this.name = name;
	}
	
	public String getBegin() {
		return begin;
	}
	
	public String getEnd() {
		return end;
	}
	
	public String getName() {
		return name;
	}
	
	public void print() {
		System.out.println(this.name + " de " + this.begin + " à " + this.end);
	}
}
