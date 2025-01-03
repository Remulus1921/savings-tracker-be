package org.bekierz.savingstrackerbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties
public class SavingsTrackerBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SavingsTrackerBeApplication.class, args);
	}

}
