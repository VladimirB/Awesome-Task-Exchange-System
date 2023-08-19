package ates.homework.auth.broker;

import ates.homework.auth.event.EventWrapper;
import ates.homework.auth.event.VersionedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaEventSender implements EventSender {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaEventSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public <T extends VersionedEvent> void sendEvent(EventWrapper<T> eventWrapper, String topic) throws JsonProcessingException {
        kafkaTemplate.send(topic, objectMapper.writeValueAsString(eventWrapper));
    }
}
