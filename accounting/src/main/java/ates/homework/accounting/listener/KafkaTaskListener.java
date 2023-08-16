package ates.homework.accounting.listener;

import ates.homework.accounting.event.EventWrapper;
import ates.homework.accounting.event.TaskWasCompletedEvent;
import ates.homework.accounting.repository.UserRepository;
import ates.homework.accounting.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "accounting-tasks-listener", topics = "task-tracker.task-lifecycle")
public class KafkaTaskListener {

    private final UserRepository userRepository;

    private final AccountService accountService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaTaskListener(UserRepository userRepository, AccountService accountService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
    }

    @KafkaHandler
    void listener(String data) throws JSONException, JsonProcessingException {
        JSONObject json = new JSONObject(data);
        var eventName = json.getString("name");
        switch (eventName) {
            case TaskWasCompletedEvent.NAME -> proceedTaskWasCompleted(json);
        }
    }

    private void proceedTaskWasCompleted(JSONObject json) throws JsonProcessingException, JSONException {
        switch (json.getInt("version")) {
            case TaskWasCompletedEvent.VERSION -> {
                EventWrapper<TaskWasCompletedEvent> event = objectMapper.readValue(json.toString(), new TypeReference<>() {});
                accountService.updateBalance(event.getData());
            }
        };
    }
}
