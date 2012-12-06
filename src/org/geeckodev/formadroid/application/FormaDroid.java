package org.geeckodev.formadroid.application;

import org.geeckodev.formadroid.model.Model;

import android.app.Application;

public class FormaDroid extends Application {
	public Model model;

	public FormaDroid() {
		this.model = new Model();
	}
}
