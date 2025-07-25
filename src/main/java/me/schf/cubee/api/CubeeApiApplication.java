package me.schf.cubee.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CubeeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CubeeApiApplication.class, args);
	}

}
