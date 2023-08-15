package ates.homework.auth.config;

import ates.homework.auth.broker.KafkaEventSender;
import ates.homework.auth.broker.EventSender;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventSenderConfig {

    @Bean
    public EventSender eventSender(KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaEventSender(kafkaTemplate);
    }
}
