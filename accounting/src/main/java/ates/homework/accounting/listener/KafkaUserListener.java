package ates.homework.accounting.listener;

import ates.homework.accounting.entity.User;
import ates.homework.accounting.entity.UserRole;
import ates.homework.accounting.repository.UserRepository;
import ates.homework.accounting.service.AccountService;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "accounting-users-listener", topics = "users-stream")
public class KafkaUserListener {

    private final UserRepository userRepository;

    private final AccountService accountService;

    public KafkaUserListener(UserRepository userRepository, AccountService accountService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
    }

    @KafkaHandler
    void listener(String data) throws JSONException {
        JSONObject json = new JSONObject(data);
        var eventName = json.getString("name");
        if ("UserWasCreated".equals(eventName)) {
            var user = createUser(json.getJSONObject("data"));
            var savedUser = userRepository.save(user);
            accountService.createAccount(savedUser);
        }
    }

    private User createUser(JSONObject json) throws JSONException {
        return new User(json.getString("publicId"),
                json.getString("login"),
                UserRole.valueOf(json.getString("role")));
    }
}
