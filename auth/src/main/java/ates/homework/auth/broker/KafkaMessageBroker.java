package ates.homework.auth.broker;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

public class KafkaMessageBroker implements MessageBroker {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaMessageBroker(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message, String topic) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
