package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class NewCustomerDto {

    @JsonProperty("firstName")
    public String firstName;

    @JsonProperty("lastName")
    public String lastName;

    @JsonProperty("email")
    public String email;
}
