package io.yharsh.coffeemachine.input.commands;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.yharsh.coffeemachine.domain.input.commands.Command;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class CommandTest {
    @Test
    public void testCommandForVariousTypes() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String commandStr = "[\n" +
                "  {\n" +
                "    \"operation\": \"ADD_INGREDIENTS\",\n" +
                "    \"total_items_quantity\": {\n" +
                "      \"milk\": 10\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"operation\": \"ADD_RECIPE\",\n" +
                "    \"name\": \"Coffee\",\n" +
                "    \"ingredients\": {\n" +
                "      \"milk\": 10,\n" +
                "      \"sugar\": 1,\n" +
                "      \"coffee\": 9\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"operation\": \"PREPARE_BEVERAGES\",\n" +
                "    \"names\": [\"coffee\"]\n" +
                "  }\n" +
                "]";
        List<Command> commands = objectMapper.readValue(commandStr, new TypeReference<List<Command>>() {
        });
        System.out.println(commands);
    }
}
