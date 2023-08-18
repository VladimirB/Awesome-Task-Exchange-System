package ates.homework.accounting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TotalRevenueReport(@JsonProperty("revenue") int revenue,
                                 @JsonProperty("accounts_with_losses") List<AccountShortDto> accounts) {
}
