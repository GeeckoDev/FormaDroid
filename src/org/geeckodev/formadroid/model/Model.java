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
	private String selectedEstt;
	private String selectedDept;
	private String selectedGroup;
	private DAO dao;
	private boolean pending;

	public Model() {
		this.estts = new ArrayList<Establishment>();
		this.depts = new ArrayList<Department>();
		this.groups = new ArrayList<Group>();
		this.days = new ArrayList<Day>();
		this.selectedEstt = null;
		this.selectedDept = null;
		this.selectedGroup = null;
		this.dao = new DAO();
		this.pending = false;
	}

	public void selectEstablishment(String estt) {
		this.selectedEstt = estt;
	}

	public void selectDepartment(String dept) {
		this.selectedDept = dept;
	}

	public void selectGroup(String group) {
		this.selectedGroup = group;
	}

	public List<Establishment> getEstablishments() {
		return this.estts;
	}

	public List<Department> getDepartments() {
		return this.depts;
	}

	public List<Group> getGroups() {
		return this.groups;
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

	public void buildEstablishments() throws IOException {
		this.pending = true;

		try {
			this.dao.findEstablishments(this.estts);
		} catch (IOException e) {
			this.pending = false;
			throw e;
		}

		this.pending = false;
	}

	public void buildDepartments() throws IOException {
		this.pending = true;

		try {
			this.dao.findDepartments(this.selectedEstt, this.depts);
		} catch (IOException e) {
			this.pending = false;
			throw e;
		}

		this.pending = false;
	}

	public void buildGroups() throws IOException {
		this.pending = true;

		try {
			this.dao.findGroups(this.selectedEstt, this.selectedDept,
					this.groups);
		} catch (IOException e) {
			this.pending = false;
			throw e;
		}

		this.pending = false;
	}

	public void buildDays() throws IOException {
		this.pending = true;

		try {
			this.dao.findDays(this.selectedEstt, this.selectedDept,
					this.selectedGroup, this.days);
		} catch (IOException e) {
			this.pending = false;
			throw e;
		}

		this.pending = false;
	}
}
