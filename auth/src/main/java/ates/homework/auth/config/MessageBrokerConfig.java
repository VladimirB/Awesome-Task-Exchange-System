package ates.homework.auth.config;

import ates.homework.auth.broker.FakeMessageBroker;
import ates.homework.auth.broker.MessageBroker;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MessageBrokerConfig {

    @Bean
    public MessageBroker messageBroker() {
        return new FakeMessageBroker();
    }
}
