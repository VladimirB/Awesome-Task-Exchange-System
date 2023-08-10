package ates.homework.auth.dto;

import ates.homework.auth.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDto(@JsonProperty(required = true) String login,
                      @JsonProperty(required = true) String password,
                      @JsonProperty(required = true) String role) {
}
