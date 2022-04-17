package com.covid.countries.statistics.repository;

import org.springframework.data.repository.CrudRepository;

import com.covid.countries.statistics.ui.model.response.Covid19Stats;

public interface StatisticsRepository extends CrudRepository<Covid19Stats, Integer> {

}
