package ates.homework.registry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;

public class JsonSchemaUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final JsonSchemaProvider provider = new JsonSchemaProvider();

    public boolean isValid(String content, String eventName, int eventVersion) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(content);
        JsonSchema schema = provider.provide(eventName, eventVersion);

        var validations = schema.validate(node);
        validations.forEach(it -> System.out.println("Validation warning: " + it.getMessage()));
        return validations.isEmpty();
    }
}
