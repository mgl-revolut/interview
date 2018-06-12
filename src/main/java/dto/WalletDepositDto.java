package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class WalletDepositDto {

    @JsonProperty("amount")
    public String amount;
}
