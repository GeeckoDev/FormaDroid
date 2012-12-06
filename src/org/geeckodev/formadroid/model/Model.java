package org.geeckodev.formadroid.model;

import java.io.IOException;
import java.util.Date;

import org.geeckodev.formadroid.dao.DAO;

public class Model {
	private String[] groups = { "A", "B", "C", "D", "GI", "ID", "IE", "LP" };
	private String group;
	private Week week;
	private Week nextWeek;
	private DAO dao;
	private boolean pending;

	public Model() {
		this.week = new Week();
		this.nextWeek = new Week();
		this.dao = new DAO();
		this.group = this.groups[0];
		this.pending = false;
	}

	public void selectGroup(String group) {
		this.group = group;
	}

	public Day getCurrentDay(int offset) {
		int current = (new Date().getDay() + 6) % 7;

		if (current + offset >= this.week.size()) {
			return null;
		}

		return this.week.getDay(current + offset);
	}

	public String[] getGroups() {
		return this.groups;
	}

	public Week getWeek() {
		return this.week;
	}

	public Week getNextWeek() {
		return this.nextWeek;
	}

	public boolean isPending() {
		return this.pending;
	}

	public void build() throws IOException {
		this.pending = true;

		try {
			this.dao.find(this.group, week, nextWeek);
		} catch (IOException e) {
			this.pending = false;
			throw e;
		}

		this.pending = false;
	}

	public void print() {
		this.week.print();
		this.nextWeek.print();
	}
}
