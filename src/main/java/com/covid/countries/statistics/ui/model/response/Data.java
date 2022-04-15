package com.covid.countries.statistics.ui.model.response;

import java.util.ArrayList;

public class Data {
	private String lastChecked;
	ArrayList<Covid19Stats> covid19Stats = new ArrayList<Covid19Stats>();

	public String getLastChecked() {
		return lastChecked;
	}

	public ArrayList<Covid19Stats> getCovid19Stats() {
		return covid19Stats;
	}

	public void setCovid19Stats(ArrayList<Covid19Stats> covid19Stats) {
		this.covid19Stats = covid19Stats;
	}

	public void setLastChecked(String lastChecked) {
		this.lastChecked = lastChecked;
	}
}
