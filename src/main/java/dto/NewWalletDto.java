package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class NewWalletDto {

    @JsonProperty("currency")
    public String walletCurrency;
}
