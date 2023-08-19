package ates.homework.auth.broker;

import ates.homework.auth.event.EventWrapper;
import ates.homework.auth.event.VersionedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface EventSender {

    <T extends VersionedEvent> void sendEvent(EventWrapper<T> eventWrapper, String topic) throws JsonProcessingException;
}