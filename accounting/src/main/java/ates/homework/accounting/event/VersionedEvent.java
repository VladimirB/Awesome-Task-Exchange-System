package ates.homework.accounting.event;

public interface VersionedEvent {

    String getName();

    int getVersion();
}
