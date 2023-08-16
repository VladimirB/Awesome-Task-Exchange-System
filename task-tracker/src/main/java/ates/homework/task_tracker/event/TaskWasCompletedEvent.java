package ates.homework.task_tracker.event;

import ates.homework.task_tracker.broker.VersionedEvent;

public record TaskWasCompletedEvent(String taskPublicId,
                                    String title,
                                    int payout,
                                    String userPublicId) implements VersionedEvent {

    private static final String NAME = "TaskWasCompleted";
    private static final int VERSION = 1;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getVersion() {
        return VERSION;
    }
}
