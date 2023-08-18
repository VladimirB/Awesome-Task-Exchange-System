package ates.homework.accounting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountShortDto(@JsonProperty("user_name") String userName,
                              @JsonProperty("balance") int balance) {
}
