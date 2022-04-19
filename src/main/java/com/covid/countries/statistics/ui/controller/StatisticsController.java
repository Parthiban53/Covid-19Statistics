package com.covid.countries.statistics.ui.controller;

import java.util.List;

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

@RestController
public class StatisticsController {

	@Autowired
	StatisticsService statsService;

	@GetMapping(value = "/getStatisticsForAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Covid19Stats>> getStatisticsForAll() throws Exception {
		List<Covid19Stats> covid19Stats = Util.getAPIData(Util.BASE_URL);
		if (null != covid19Stats) {
			statsService.saveStatistics(covid19Stats);
			return new ResponseEntity<List<Covid19Stats>>(covid19Stats, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Covid19Stats>>(HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping(value = "/getStatisticsForCountry/{country}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Covid19Stats>> getStatisticsForCountry(@PathVariable("country") String country,
			@RequestParam(value = "pageNumber", required = true) int pageNumber,
			@RequestParam(value = "pageSize", required = true) int pageSize) throws Exception {
		String url = Util.BASE_URL + "?country=" + country;
		List<Covid19Stats> covid19Stats = Util.getAPIData(url);
		List<Covid19Stats> filteredCovid19Stats = covid19Stats != null
				? covid19Stats.subList((pageNumber-1) * pageSize,
						Math.min(((pageNumber-1) + 1) * pageSize, covid19Stats.size()))
				: null;
		if (null != filteredCovid19Stats) {
			return new ResponseEntity<List<Covid19Stats>>(filteredCovid19Stats, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Covid19Stats>>(HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping(value = "/getStatisticsFromH2", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Covid19Stats>> getStatisticsFromH2() throws Exception {
		List<Covid19Stats> covid19Stats = statsService.getStatistics();
		if (null != covid19Stats) {
			return new ResponseEntity<List<Covid19Stats>>(covid19Stats, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Covid19Stats>>(HttpStatus.NO_CONTENT);
		}
	}
}
