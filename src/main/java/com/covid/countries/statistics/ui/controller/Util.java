package com.covid.countries.statistics.ui.controller;

import java.io.IOException;
import java.util.List;

import com.covid.countries.statistics.ui.model.response.Covid19Stats;
import com.covid.countries.statistics.ui.model.response.StatisticsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Util {
	
	public static final String BASE_URL = "https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/stats";
	
	public static List<Covid19Stats> getAPIData(String url)
			throws IOException, JsonProcessingException, JsonMappingException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).get()
				.addHeader("X-RapidAPI-Host", "covid-19-coronavirus-statistics.p.rapidapi.com")
				.addHeader("X-RapidAPI-Key", "b5b7b30590msh9ae848af01a7cfdp1d03d4jsn0d4fe5f504e6").build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful())
			throw new IOException("Unexpected code " + response);
		String rawResponse = response.body().string();
		List<Covid19Stats> covid19Stats = null;
		if (null != rawResponse && !rawResponse.isEmpty()) {
			StatisticsResponse sResponse = new ObjectMapper().readValue(rawResponse, StatisticsResponse.class);
			covid19Stats = sResponse.getData() != null ? sResponse.getData().getCovid19Stats() : null;
		}
		return covid19Stats;
	}

}
