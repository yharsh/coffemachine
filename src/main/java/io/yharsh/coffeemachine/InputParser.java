package io.yharsh.coffeemachine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.yharsh.coffeemachine.domain.input.InitializationInput;
import io.yharsh.coffeemachine.domain.input.commands.Command;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class InputParser {
    private ObjectMapper objectMapper;

    public InputParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public InitializationInput getInitializationInput(ApplicationArguments applicationArguments) throws IOException {
        return objectMapper.readValue(new File(applicationArguments.getOptionValues("initFile").get(0)), InitializationInput.class);
    }

    public List<Command> getCommands(ApplicationArguments applicationArguments) throws IOException {
        return objectMapper.readValue(new File(applicationArguments.getOptionValues("commandFile").get(0)),
                new TypeReference<List<Command>>() {
                });
    }
}
