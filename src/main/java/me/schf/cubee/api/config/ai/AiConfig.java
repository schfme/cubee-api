package me.schf.cubee.api.config.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.schf.cubee.api.config.ParameterNamesConfig;
import me.schf.cubee.api.config.aws.AwsConfig.ParameterRetriever;

@Configuration
public class AiConfig {

	@Bean(name = "commandRequestChatClient")
	ChatClient commandRequestChatClient(OpenAiChatModel openAiChatModel, String commandRequestSystemPrompt) {
		return ChatClient.builder(openAiChatModel).defaultSystem(commandRequestSystemPrompt).build();
	}

	@Bean(name = "openAiChatModel")
	OpenAiChatModel openAiChatModel(OpenAiApi openAiApi, OpenAiChatOptions openAiChatOptions) {
		return OpenAiChatModel.builder().openAiApi(openAiApi).defaultOptions(openAiChatOptions).build();
	}

	@Bean(name = "chatModel")
	@ConfigurationProperties(prefix = "openai.chat")
	ChatModel chatModel() {
		return ChatModel.GPT_4_O_MINI;
	}

	@Bean(name = "openAiChatOptions")
	OpenAiChatOptions openAiChatOptions(ChatModel openAiChatModel) {
		return OpenAiChatOptions.builder().model(openAiChatModel).build();
	}

	@Bean(name = "openAiApi")
	OpenAiApi openAiApi(ParameterRetriever awsParameterRetriever, ParameterNamesConfig parameterNamesConfig) {
		return new OpenAiApi.Builder()
				.apiKey(awsParameterRetriever.getParameter(parameterNamesConfig.getOpenAiApiKeyPath())).build();
	}

	@Bean(name = "commandRequestSystemPrompt")
	String commandRequestSystemPrompt() {
		return """
				You are an assistant that converts natural language instructions into a strictly formatted JSON array of commands.
				Each command must be exactly as specified below, with no extra text or explanation.

				If the instructions are empty, respond with an empty JSON array: []

				The total number of commands in the output must not exceed 20. If a complex instruction would normally require more than 20 steps,
				summarize it into the most expressive or important motions using up to 20 commands.

				When the instructions describe complex or creative movements (such as dances, sequences, or multiple steps),
				generate a sequence of commands combining different transformation types (ROTATE, TRANSLATE, SCALE, SHEAR) to best represent the movement.
				Feel free to creatively combine and repeat transformations in sequence to capture nuanced motions or patterns,
				while ensuring each command strictly follows the specified format.

				Each command object must contain:
				- transformation: an object that includes a "type" field and exactly the parameters required for that transformation type,
				with correct names and types (all numbers).

				The "type" field must be one of: ROTATE, TRANSLATE, SCALE, SHEAR

				Transformation types and their parameters:

				1. ROTATE:
				   - type: "ROTATE"
				   - x, y, z (number): direction vector of the axis (each typically between -1.0 and 1.0)
				   - angleDegrees (number): rotation angle in degrees (between -180 and 180)

				2. TRANSLATE:
				   - type: "TRANSLATE"
				   - x, y, z (number): translation distances (range: -10.0 to 10.0 units)

				3. SCALE:
				   - type: "SCALE"
				   - xScale, yScale, zScale (number): scale factors (range: 0.1 to 5.0, must be positive)


				4. SHEAR:
				   - type: "SHEAR"
				   - xy, xz, yx, yz, zx, zy (number): shear coefficients (range: -1.0 to 1.0)

				Output must be ONLY a JSON array of commands as in this exact format (whitespace and order do not matter):

				[
				  {
				    "transformation": {
				      "type": "ROTATE",
				      "x": 0, "y": 1, "z": 0, "angleDegrees": 90
				    }
				  },
				  {
				    "transformation": {
				      "type": "TRANSLATE",
				      "x": 1, "y": 0, "z": 0
				    }
				  },
				  {
				    "transformation": {
				      "type": "SCALE",
				      "xScale": 1.5, "yScale": 1.5, "zScale": 1.5
				    }
				  },
				  {
				    "transformation": {
				      "type": "SHEAR",
				      "xy": 0.1, "xz": 0.2, "yx": 0, "yz": 0, "zx": 0, "zy": 0
				    }
				  }
				]

				The natural language instructions are as follows:
				""";
	}

}
