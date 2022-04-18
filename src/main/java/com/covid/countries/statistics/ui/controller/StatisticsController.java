package com.covid.countries.statistics.ui.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.covid.countries.statistics.service.StatisticsService;
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

	@Autowired
	StatisticsService statsService;

	@GetMapping(value = "/getCovidStatisticsReportForAllCountries", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Covid19Stats>> getCovidStatisticsReportForAllCountries() throws Exception {
		List<Covid19Stats> covid19Stats = getAPIData(Util.BASE_URL);
		if (null != covid19Stats) {
			statsService.saveStatistics(covid19Stats);
			return new ResponseEntity<List<Covid19Stats>>(covid19Stats, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Covid19Stats>>(HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping(value = "/getCovidStatisticsReportForSpecificCountry/{country}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Covid19Stats>> getCovidStatisticsReportForSpecificCountry(@PathVariable("country") String country,
			@RequestParam(value = "pageNumber", required = true) int pageNumber,
			@RequestParam(value = "pageSize", required = true) int pageSize) throws Exception {
		String url = Util.BASE_URL + "?country=" + country;
		List<Covid19Stats> covid19Stats = getAPIData(url);
		if (null != covid19Stats) {
			Map<Integer, List<Covid19Stats>> covid19StatsMap = IntStream
					.range(0, (covid19Stats.size() + pageSize - 1) / pageSize).boxed()
					.collect(Collectors.toMap(i -> i, i -> covid19Stats.subList(i * pageSize,
							Math.min((i + 1) * pageSize, covid19Stats.size()))));
			List<Covid19Stats> filteredCovid19Stats = (null != covid19StatsMap)
					? null != covid19StatsMap.get(pageNumber-1) ? covid19StatsMap.get(pageNumber-1) : null
					: null;
			if (null != filteredCovid19Stats) {
				return new ResponseEntity<List<Covid19Stats>>(filteredCovid19Stats, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<Covid19Stats>>(HttpStatus.NO_CONTENT);
			}
		} else {
			return new ResponseEntity<List<Covid19Stats>>(HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping(value = "/getCovidStatisticsReportForAllCountriesFromH2DB", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Covid19Stats>> getCovidStatisticsReportForAllCountriesFromH2DB() throws Exception {
		List<Covid19Stats> covid19Stats = statsService.getStatistics();
		if (null != covid19Stats) {
			return new ResponseEntity<List<Covid19Stats>>(covid19Stats, HttpStatus.OK);
		} else {
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
