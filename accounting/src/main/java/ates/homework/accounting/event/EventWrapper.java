package ates.homework.accounting.event;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.UUID;

public class EventWrapper<T extends VersionedEvent> {

    private String id;
    private String name;
    private int version;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date eventDate;

    private T data;

    public EventWrapper() {

    }

    public EventWrapper(T data) {
        id = UUID.randomUUID().toString();
        name = data.getName();
        version = data.getVersion();
        eventDate = new Date();
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public Date getEventDate() {
        return eventDate;
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
                "id='" + id +
                ", name='" + name + '\'' +
                ", version=" + version +
                ", eventDate=" + eventDate +
                ", data=" + data +
                '}';
    }
}