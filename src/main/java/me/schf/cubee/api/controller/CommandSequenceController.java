package me.schf.cubee.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import me.schf.cubee.api.ai.service.AiCommandRequester;
import me.schf.cubee.api.model.Command;
import me.schf.cubee.api.model.CommandSequence;

@RestController
@RequestMapping("/commands")
@Tag(name = "Command Sequence Generator", description = "Generate transformation commands from natural language instructions")
@Validated
public class CommandSequenceController {

	private final AiCommandRequester commandRequester;
	private final Logger logger = LoggerFactory.getLogger(CommandSequenceController.class);

	public CommandSequenceController(AiCommandRequester commandRequester) {
		this.commandRequester = commandRequester;
	}

	@PostMapping
	@Operation(
			summary = "Generate cube transformation commands", 
			description = "Takes a natural language string and returns a list of structured transformation commands (rotate, translate, scale, shear)", 
			responses = {
			@ApiResponse(responseCode = "200", 
						 description = "Successful transformation", 
						 content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommandSequence.class))),
			@ApiResponse(responseCode = "400", 
						 description = "Invalid input", 
						 content = @Content) })
	public CommandSequence generateCommandSequence(@RequestBody @NotBlank String instructionText) {
		List<Command> commands;
		try {
			commands = commandRequester.askForCommands(instructionText);
		} catch (JsonProcessingException e) {
			logger.error(e.getLocalizedMessage());
			return CommandSequence.empty();
		}

		return new CommandSequence(commands);
	}
	
	
}