package com.trifork.fhir_kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FhirKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FhirKafkaApplication.class, args);
	}

}
 