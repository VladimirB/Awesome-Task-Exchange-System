package ates.homework.accounting.listener;

import ates.homework.accounting.entity.User;
import ates.homework.accounting.entity.UserRole;
import ates.homework.accounting.repository.UserRepository;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "class-level", topics = "users-stream")
public class KafkaUserListener {

    private final UserRepository userRepository;

    public KafkaUserListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @KafkaHandler
    void listener(String data) throws JSONException {
        JSONObject json = new JSONObject(data);
        var eventName = json.getString("name");
        if ("UserWasCreated".equals(eventName)) {
            var user = createUser(json.getJSONObject("data"));
            userRepository.save(user);
        }
    }

    private User createUser(JSONObject json) throws JSONException {
        return new User(json.getString("publicId"),
                json.getString("login"),
                UserRole.valueOf(json.getString("role")));
    }
}
