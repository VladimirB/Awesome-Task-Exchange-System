package ates.homework.task_tracker.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record TaskWasCompletedEvent(String taskPublicId,
                                    String title,
                                    int payout,
                                    String userPublicId) implements VersionedEvent {

    private static final String NAME = "task-tracker.TaskWasCompleted";
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
