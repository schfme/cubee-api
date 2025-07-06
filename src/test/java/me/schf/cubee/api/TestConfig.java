package me.schf.cubee.api;

import java.util.Map;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import me.schf.cubee.api.config.aws.AwsConfig.ParameterRetriever;

@TestConfiguration
public class TestConfig {

	@Primary
	@Bean("testAwsParameterRetriever")  
	ParameterRetriever awsParameterRetriever() {
		return new ParameterRetriever() {
			
			@Override
			public Map<String, String> getParametersByPath(String path, boolean recursive) {
				return Map.of("map-key", "dummy-api-key");
			}
			
			@Override
			public String getParameter(String parameterName) {
				return "dummy-parameter";
			}
		};
	}
}
