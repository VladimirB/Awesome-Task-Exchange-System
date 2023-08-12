package ates.homework.auth.broker;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface EventSender {

    <T> void sendEvent(Event<T> event, String topic) throws JsonProcessingException;
}