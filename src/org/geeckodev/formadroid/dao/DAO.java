package org.geeckodev.formadroid.dao;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.geeckodev.formadroid.model.Lesson;
import org.geeckodev.formadroid.model.Week;

public class DAO {
	private final String url = "http://vpnetudiant.fr/formafetch/";
	private DefaultHttpClient client;

	public DAO() {
		this.client = new DefaultHttpClient();
	}

	private String retrieve(String url) throws IOException {
		HttpGet getRequest = new HttpGet(url);

		HttpResponse getResponse = client.execute(getRequest);
		final int statusCode = getResponse.getStatusLine().getStatusCode();

		if (statusCode != HttpStatus.SC_OK) {
			getRequest.abort();
			throw new IOException();
		}

		HttpEntity getResponseEntity = getResponse.getEntity();

		if (getResponseEntity == null) {
			getRequest.abort();
			throw new IOException();
		}

		return EntityUtils.toString(getResponseEntity);
	}

	private void build(String data, Week week) {
		int day = -1;

		week.clear();

		for (String i : data.split("\n")) {
			if (i.startsWith(">")) {
				day++;
				continue;
			}

			String begin = i.substring(0, 5);
			String end = i.substring(6, 11);
			String name = i.substring(12, i.length());

			week.getDay(day).addLesson(new Lesson(begin, end, name));
		}
	}

	public void find(String group, Week week, Week nextWeek) throws IOException {
		build(retrieve(this.url + group), week);
		// build(retrieve(this.url + group + "_next"), nextWeek);
	}

}
