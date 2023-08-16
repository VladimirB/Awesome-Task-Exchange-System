package ates.homework.task_tracker.broker;

public interface VersionedEvent {

    String getName();

    int getVersion();
}
