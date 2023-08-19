package ates.homework.task_tracker.broker;

import ates.homework.task_tracker.event.EventWrapper;
import ates.homework.task_tracker.event.VersionedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface EventSender {

    <T extends VersionedEvent> void sendEvent(EventWrapper<T> eventWrapper, String topic) throws JsonProcessingException;
}