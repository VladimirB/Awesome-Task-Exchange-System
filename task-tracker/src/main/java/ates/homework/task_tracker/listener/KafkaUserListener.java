package ates.homework.task_tracker.listener;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "class-level", topics = "users-stream")
public class KafkaUserListener {

    @KafkaHandler
    void listener(String data) {
        System.out.println("YEP!!!");
    }
}
