package me.schf.cubee.api.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.fasterxml.jackson.core.JsonProcessingException;

import me.schf.cubee.api.TestConfig;
import me.schf.cubee.api.ai.service.AiCommandRequester;
import me.schf.cubee.api.config.ParameterNamesConfig;
import me.schf.cubee.api.model.Command;
import me.schf.cubee.api.model.CommandSequence;
import me.schf.cubee.api.model.Rotate;

@Import({TestConfig.class, ParameterNamesConfig.class})
@WebMvcTest(CommandSequenceController.class)
class CommandSequenceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AiCommandRequester commandRequester;
    
    private RequestPostProcessor apiKeyHeader() {
        return request -> {
            request.addHeader("X-API-Key", "dummy-api-key");
            return request;
        };
    }

    @Test
    void test_generateCommandSequence_validInstruction_shouldReturnCommands() throws Exception {
    	Rotate roate = new Rotate(1.0, 0.0, 0.0, 90.0);
        Command mockCommand = new Command(roate);
        CommandSequence mockSequence = new CommandSequence(List.of(mockCommand));

        when(commandRequester.askForCommands(Mockito.anyString()))
            .thenReturn(mockSequence.commands());

        mockMvc.perform(post("/commands")
                .with(apiKeyHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"rotate the cube right\""))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.commands").isArray())
            .andExpect(jsonPath("$.commands[0].transformation.type").value("ROTATE"))
            .andExpect(jsonPath("$.commands[0].transformation.x").value(1.0))
            .andExpect(jsonPath("$.commands[0].transformation.y").value(0.0))
            .andExpect(jsonPath("$.commands[0].transformation.z").value(0.0))
            .andExpect(jsonPath("$.commands[0].transformation.angleDegrees").value(90.0));
    }

    @Test
    void test_generateCommandSequence_blankInstruction_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/commands")
                .with(apiKeyHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("")) // blank string
            .andExpect(status().isBadRequest());
    }

    @Test
    void test_generateCommandSequence_invalidJson_shouldReturnEmptyCommands() throws Exception {
        mockMvc.perform(post("/commands")
                .with(apiKeyHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"foo\": }"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.commands").isArray())
            .andExpect(jsonPath("$.commands").isEmpty());
    }

    @Test
    void test_generateCommandSequence_exceptionThrown_shouldReturnEmptyCommands() throws Exception {
        when(commandRequester.askForCommands(anyString()))
            .thenThrow(new JsonProcessingException("Mock parse error") {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;});

        mockMvc.perform(post("/commands")
                .with(apiKeyHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"rotate the cube right\""))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.commands").isArray())
            .andExpect(jsonPath("$.commands").isEmpty());
    }
}
