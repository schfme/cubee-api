package me.schf.cubee.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.parameter-names")
public class ParameterNamesConfig {

	private String apiKeysPath;
	private String openAiApiKeyPath;

	public String getApiKeysPath() {
		return apiKeysPath;
	}

	public String getOpenAiApiKeyPath() {
		return openAiApiKeyPath;
	}

	public void setOpenAiApiKeyPath(String openAiApiKeyPath) {
		this.openAiApiKeyPath = openAiApiKeyPath;
	}

	public void setApiKeysPath(String apiKeysPath) {
		this.apiKeysPath = apiKeysPath;
	}

}