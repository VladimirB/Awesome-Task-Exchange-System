package ates.homework.task_tracker.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record TaskWasAssignedEvent(String taskPublicId,
                                   String title,
                                   int penalty,
                                   String userPublicId) implements VersionedEvent {

    private static final String NAME = "TaskWasAssigned";
    private static final int VERSION = 1;

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
