package utils;

import dto.NewAccountDto;
import dto.NewCustomerDto;
import dto.TransferMoneyBetweenAccountsDto;
import dto.WalletDepositDto;
import model.Wallet;
import service.AccountService;

import java.util.UUID;

public final class TestHelper {

    public static NewCustomerDto createNewCustomerRequest(String firstName, String lastName, String email) {

        NewCustomerDto newCustomerDto = new NewCustomerDto();
        newCustomerDto.firstName = firstName;
        newCustomerDto.lastName = lastName;
        newCustomerDto.email = email;

        return newCustomerDto;
    }

    public static NewAccountDto createNewAccountRequest(String customerId) {

        NewAccountDto newAccountDto = new NewAccountDto();
        newAccountDto.customerId = customerId;
        return newAccountDto;
    }

    public static WalletDepositDto createWalletDepositRequest(String amount) {

        WalletDepositDto walletDepositDto = new WalletDepositDto();
        walletDepositDto.amount = amount;
        return walletDepositDto;
    }


    public static TransferMoneyBetweenAccountsDto createTransferMoneyBetweenAccountsRequest(String sourceWalletId,
                                                                                      String destinationWalletId,
                                                                                      String amount) {

        TransferMoneyBetweenAccountsDto transferMoneyBetweenAccountsDto = new TransferMoneyBetweenAccountsDto();
        transferMoneyBetweenAccountsDto.sourceWalletId = sourceWalletId;
        transferMoneyBetweenAccountsDto.destinationWalletId = destinationWalletId;
        transferMoneyBetweenAccountsDto.amount = amount;
        return transferMoneyBetweenAccountsDto;
    }

    public static Wallet getWalletForAccount(AccountService accountService, UUID accountId, UUID walletId) {

        return accountService.getAllWalletsFromAccount(accountId)
                   .orElseThrow(() -> new RuntimeException("Wallet is missing. Account has no wallets."))
                   .stream()
                   .filter(wallet -> wallet.getWalletId().equals(walletId))
                   .findAny()
                   .orElseThrow(() -> new RuntimeException("Wallet is missing."));
    }
}
