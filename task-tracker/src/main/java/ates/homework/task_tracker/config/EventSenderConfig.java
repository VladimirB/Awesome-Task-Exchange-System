package ates.homework.task_tracker.config;

import ates.homework.task_tracker.broker.EventSender;
import ates.homework.task_tracker.broker.KafkaEventSender;
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
