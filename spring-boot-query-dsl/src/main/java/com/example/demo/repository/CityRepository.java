package com.example.demo.repository;

import com.example.demo.model.City;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends CrudRepository<City, Long>
        , QuerydslPredicateExecutor<City> {
}
