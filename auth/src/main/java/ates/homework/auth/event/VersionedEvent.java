package ates.homework.auth.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface VersionedEvent {

    @JsonIgnore
    String getName();

    @JsonIgnore
    int getVersion();
}
