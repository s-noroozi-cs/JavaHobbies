package com.example.demo;

import com.example.demo.model.QCity;
import com.example.demo.repository.CityRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class DemoApplicationTests {
//	@PersistenceContext
//	private EntityManager entityManager;

	@Autowired
	private CityRepository cityRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void exec_query_using_repo(){
		var qCity = QCity.city;
		BooleanExpression booleanExpression = qCity.population.goe(2_000_000);
		OrderSpecifier<String> orderSpecifier = qCity.name.asc();
		var cities2 = cityRepository.findAll(booleanExpression, orderSpecifier);
		log.info("{}", cities2);
	}



}
