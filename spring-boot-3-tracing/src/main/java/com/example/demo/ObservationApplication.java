package com.example.demo;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class ObservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObservationApplication.class, args);
	}

	@Bean
	ApplicationListener<ApplicationStartedEvent> doOnStart(ObservationRegistry registry) {
		return event -> generateString(registry);
	}

	public void generateString(ObservationRegistry registry) {
		String something = Observation
				.createNotStarted("server.job", registry)
				.lowCardinalityKeyValue("jobType", "string")

				.observe(() -> {
					log.info("Generating a String...");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						return "NOTHING";
					}
					return "SOMETHING";
				});

		log.info("Result was: " + something);
	}

}
