package ates.homework.accounting.listener;

import ates.homework.accounting.entity.User;
import ates.homework.accounting.event.EventWrapper;
import ates.homework.accounting.event.UserWasCreatedEvent;
import ates.homework.accounting.repository.UserRepository;
import ates.homework.accounting.service.AccountService;
import ates.homework.registry.JsonSchemaUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "accounting-users-listener", topics = "users-stream")
public class KafkaUserListener {

    private final UserRepository userRepository;

    private final JsonSchemaUtil jsonSchemaUtil = new JsonSchemaUtil();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaUserListener(UserRepository userRepository, AccountService accountService) {
        this.userRepository = userRepository;
    }

    @KafkaHandler
    void listener(String data) throws JSONException, JsonProcessingException {
        if (jsonSchemaUtil.isValid(data, UserWasCreatedEvent.NAME, UserWasCreatedEvent.VERSION)) {
            EventWrapper<UserWasCreatedEvent> event = objectMapper.readValue(data, new TypeReference<>() {});

            var user = new User();
            user.setPublicId(event.getData().publicId());
            user.setLogin(event.getData().login());
            user.setRole(event.getData().role());
            userRepository.save(user);
        } else {
            // alerts, logs, some actions etc.
        }
    }
}
