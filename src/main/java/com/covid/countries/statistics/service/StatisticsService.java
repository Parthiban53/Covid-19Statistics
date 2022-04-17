package com.covid.countries.statistics.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.covid.countries.statistics.repository.StatisticsRepository;
import com.covid.countries.statistics.ui.model.response.Covid19Stats;

@Service
public class StatisticsService {

	@Autowired
	StatisticsRepository statsRepository;

	public List<Covid19Stats> getStatistics() {
		List<Covid19Stats> covid19Stats = new ArrayList<Covid19Stats>();
		statsRepository.findAll().forEach(stats -> covid19Stats.add(stats));
		return covid19Stats;
	}

	public Covid19Stats getStatisticsById(int id) {
		return statsRepository.findById(id).get();
	}

	public void saveStatistics(List<Covid19Stats> stats) {
		statsRepository.saveAll(stats);
	}

	public void deleteStatisticsById(int id) {
		statsRepository.deleteById(id);
	}

}
