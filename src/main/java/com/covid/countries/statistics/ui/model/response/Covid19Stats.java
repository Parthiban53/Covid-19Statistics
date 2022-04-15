package com.covid.countries.statistics.ui.model.response;

public class Covid19Stats {
	private String city;
	private String province;
	private String country;
	private String lastUpdate;
	private String keyId;
	private float confirmed;
	private float deaths;
	private String recovered;

	public String getCity() {
		return city;
	}

	public String getProvince() {
		return province;
	}

	public String getCountry() {
		return country;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public String getKeyId() {
		return keyId;
	}

	public float getConfirmed() {
		return confirmed;
	}

	public float getDeaths() {
		return deaths;
	}

	public String getRecovered() {
		return recovered;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public void setConfirmed(float confirmed) {
		this.confirmed = confirmed;
	}

	public void setDeaths(float deaths) {
		this.deaths = deaths;
	}

	public void setRecovered(String recovered) {
		this.recovered = recovered;
	}
}
