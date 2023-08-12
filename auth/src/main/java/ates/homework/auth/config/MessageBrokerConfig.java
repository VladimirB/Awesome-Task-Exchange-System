package ates.homework.auth.config;

import ates.homework.auth.broker.KafkaMessageBroker;
import ates.homework.auth.broker.MessageBroker;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageBrokerConfig {

    @Bean
    public MessageBroker messageBroker(KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaMessageBroker(kafkaTemplate);
    }
}
