package ates.homework.registry;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;

public class JsonSchemaProvider {

    private final JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);

    public JsonSchema provide(String eventName, int version) {
        switch (eventName) {
            case "auth.UserWasCreated" -> {
                var fileName = String.format("UserWasCreated%d.json", version);
                return factory.getSchema(JsonSchemaProvider.class.getResourceAsStream("/schema/auth/" + fileName));
            }
            case "task-tracker.TaskWasCompleted" -> {
                var fileName = String.format("TaskWasCompleted%d.json", version);
                return factory.getSchema(JsonSchemaProvider.class.getResourceAsStream("/schema/task-tracker/" + fileName));
            }
            case "task-tracker.TaskWasAssigned" -> {
                var fileName = String.format("TaskWasAssigned%d.json", version);
                return factory.getSchema(JsonSchemaProvider.class.getResourceAsStream("/schema/task-tracker/" + fileName));
            }
            default -> throw new IllegalArgumentException("Not found schema for " + eventName);
        }
    }
}
