package ates.homework.accounting.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record TaskWasAssignedEvent(String taskPublicId,
                                   String title,
                                   int penalty,
                                   String userPublicId) implements VersionedEvent {

    public static final String NAME = "task-tracker.TaskWasAssigned";
    public static final int VERSION = 1;

    @Override
    @JsonIgnore
    public String getName() {
        return NAME;
    }

    @Override
    @JsonIgnore
    public int getVersion() {
        return VERSION;
    }
}
