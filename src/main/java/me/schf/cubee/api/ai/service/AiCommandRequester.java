package me.schf.cubee.api.ai.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.schf.cubee.api.model.Command;

@Component
public class AiCommandRequester {

	private final ChatClient commandRequestChatClient;
	private final ObjectMapper objectMapper;

	public AiCommandRequester(ChatClient commandRequestChatClient, ObjectMapper objectMapper) {
		super();
		this.commandRequestChatClient = commandRequestChatClient;
		this.objectMapper = objectMapper;
	}

	public List<Command> askForCommands(String userPrompt) throws JsonProcessingException{
		
		String jsonResposne = commandRequestChatClient.prompt()
			.user(userPrompt)
			.call()
			.content();
		
		System.out.println(jsonResposne);
		
        return objectMapper.readValue(jsonResposne, new TypeReference<List<Command>>() {});

	}

}
