package ates.homework.task_tracker.broker;

public class EventWrapper<T extends VersionedEvent> {

    private final String name;
    private final int version;
    private final T data;

    public EventWrapper(T data) {
        name = data.getName();
        version = data.getVersion();
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public T getData() {
        return data;
    }
}