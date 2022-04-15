package com.covid.countries.statistics.ui.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.covid.countries.statistics.ui.model.response.Covid19Stats;
import com.covid.countries.statistics.ui.model.response.StatisticsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
public class StatisticsController {
	private static final String BASE_URL = "https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/stats";

	@GetMapping(value = "/getstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Covid19Stats>> getStatistics() throws Exception {
		List<Covid19Stats> covid19Stats = getAPIData(BASE_URL);
		if(null != covid19Stats) {
			return new ResponseEntity<List<Covid19Stats>>(covid19Stats, HttpStatus.OK);
		}else {
			return new ResponseEntity<List<Covid19Stats>>(HttpStatus.NO_CONTENT);	
		}
	}

	@GetMapping(value = "/getstatistics/{country}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Covid19Stats>> getStatistics(@PathVariable("country") String country,
			@RequestParam(value = "records", required = true) int records) throws Exception {
		String url = BASE_URL + "?country=" + country;
		List<Covid19Stats> covid19Stats = getAPIData(url);
		List<Covid19Stats> filteredCovid19Stats = covid19Stats != null
				? covid19Stats.stream().limit(records).collect(Collectors.toList())
				: null;
		if(null != covid19Stats) {
			return new ResponseEntity<List<Covid19Stats>>(filteredCovid19Stats, HttpStatus.OK);
		}else {
			return new ResponseEntity<List<Covid19Stats>>(HttpStatus.NO_CONTENT);
		}
	}

	private List<Covid19Stats> getAPIData(String url)
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
