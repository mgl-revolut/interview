package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class NewAccountDto {

    @JsonProperty("customerId")
    public String customerId;
}
