package ates.homework.accounting.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record TaskWasCompletedEvent(String taskPublicId,
                                    String title,
                                    int payout,
                                    String userPublicId) implements VersionedEvent {

    public static final String NAME = "TaskWasCompleted";
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
