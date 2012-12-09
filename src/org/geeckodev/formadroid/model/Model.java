package org.geeckodev.formadroid.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.geeckodev.formadroid.dao.DAO;

public class Model {
	private List<Establishment> estts;
	private List<Department> depts;
	private List<Group> groups;
	private List<Day> days;
	private DAO dao;
	private boolean pending;

	public Model() {
		this.estts = new ArrayList<Establishment>();
		this.depts = new ArrayList<Department>();
		this.groups = new ArrayList<Group>();
		this.days = new ArrayList<Day>();
		this.dao = new DAO();
		this.pending = false;
	}

	public Day getCurrentDay(int offset) {
		int current = (new Date().getDay() + 6) % 7;

		if (current + offset >= this.days.size()) {
			return null;
		}

		return this.days.get(current + offset);
	}

	public boolean isPending() {
		return this.pending;
	}

	public void buildDays() throws IOException {
		this.pending = true;

		try {
			this.dao.findDays("17000", "infolarochelle", "6", this.days);
		} catch (IOException e) {
			this.pending = false;
			throw e;
		}

		this.pending = false;
	}

	public void print() {
		for (Day day : this.days) {
			day.print();
		}
	}
}
