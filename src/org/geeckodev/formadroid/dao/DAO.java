package org.geeckodev.formadroid.dao;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.geeckodev.formadroid.model.Day;
import org.geeckodev.formadroid.model.Department;
import org.geeckodev.formadroid.model.Establishment;
import org.geeckodev.formadroid.model.Lesson;
import org.geeckodev.formadroid.model.Group;

public class DAO {
	private final String url = "http://vpnetudiant.fr/formafetch/";
	private DefaultHttpClient client;

	public DAO() {
		/* Use ThreadSafeClientConnManager to avoid crashes on Android 2.x */

		BasicHttpParams params = new BasicHttpParams();
		SchemeRegistry sr = new SchemeRegistry();
		PlainSocketFactory psf = PlainSocketFactory.getSocketFactory();
		sr.register(new Scheme("http", psf, 80));
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, sr);
		this.client = new DefaultHttpClient(cm, params);
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

	public void findEstablishments(List<Establishment> estts)
			throws IOException {
		String data = retrieve(this.url + "estts");

		estts.clear();

		for (String i : data.split("\n")) {
			String[] s = i.split(";");

			estts.add(new Establishment(s[0], s[1]));
		}
	}

	public void findDepartments(String estt, List<Department> depts)
			throws IOException {
		String data = retrieve(this.url + estt + "/depts");

		depts.clear();

		for (String i : data.split("\n")) {
			String[] s = i.split(";");

			depts.add(new Department(s[0], s[1]));
		}
	}

	public void findGroups(String estt, String dept, List<Group> groups)
			throws IOException {
		String data = retrieve(this.url + estt + "/" + dept + "/groups");

		groups.clear();

		for (String i : data.split("\n")) {
			String[] s = i.split(";");

			groups.add(new Group(s[0], s[1]));
		}
	}

	public void findDays(String estt, String dept, String group, List<Day> days)
			throws IOException {
		String data = retrieve(this.url + estt + "/" + dept + "/" + group);
		Day curr_day = null;

		days.clear();

		for (String i : data.split("\n")) {
			if (!i.contains(";")) {
				if (curr_day != null) {
					days.add(curr_day);
				}

				curr_day = new Day(i);
				continue;
			}

			String[] s = i.split(";");
			curr_day.addLesson(new Lesson(s[0], s[1], s[2], s[3]));
		}

		days.add(curr_day);
	}
}
