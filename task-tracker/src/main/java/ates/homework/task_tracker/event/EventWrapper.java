package ates.homework.task_tracker.event;

public class EventWrapper<T extends VersionedEvent> {

    private String name;
    private int version;
    private T data;

    public EventWrapper() {

    }

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

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EventWrapper{" +
                "name='" + name + '\'' +
                ", version=" + version +
                ", data=" + data +
                '}';
    }
}