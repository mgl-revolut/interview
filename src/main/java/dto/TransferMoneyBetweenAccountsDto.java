package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class TransferMoneyBetweenAccountsDto {

    @JsonProperty("senderWalletId")
    public String sourceWalletId;

    @JsonProperty("beneficiaryWalletId")
    public String destinationWalletId;

    @JsonProperty("amount")
    public String amount;
}
