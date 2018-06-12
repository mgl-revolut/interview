package service;

import dto.NewAccountDto;
import dto.TransferMoneyBetweenAccountsDto;
import dto.WalletDepositDto;
import model.Account;
import model.Wallet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {

    /**
     * Creates a new account for the customer given.
     * @param accountDto The necessary information needed to create a new account.
     * @return If the account creation is successful then the account id is returned.
     */
    UUID createNewAccount(NewAccountDto accountDto);

    /**
     * Retrieves an account based on its id.
     * @param accountId The account id..
     * @return Returns the account if such an account exists under
     * the provided accountId, or {@link java.util.Optional#empty()} otherwise.
     */
    Optional<Account> getAccountById(UUID accountId);

    /**
     * Creates a new wallet for the account given.
     * @param accountId The accountId for which a new wallet is going to be created.
     * @param currency The currency that the new wallet will have.
     * @return If the wallet creation is successful then the wallet id is returned.
     */
    UUID createNewWallet(UUID accountId, String currency);

    /**
     * Retrieves all the wallets that belong to a specific account.
     * @param accountId The accountId for which its wallets need to be retrieved.
     * @return Returns a list of all the wallets, or {@link java.util.Optional#empty()} otherwise.
     */
    Optional<List<Wallet>> getAllWalletsFromAccount(UUID accountId);

    /**
     * Transfers money from a source wallet to a destination wallet. The transfer happens in an atomic fashion to ensure
     * validity. If the atomicity of the transaction cannot be guaranteed then an exception will be thrown to indicate
     * that the operation was aborted.
     * @param transferMoneyBetweenAccountsDto The necessary information needed to make the transfer.
     */
    void transferFundsBetweenAccounts(TransferMoneyBetweenAccountsDto transferMoneyBetweenAccountsDto);

    /**
     * Performs a deposit transaction to a specified wallet.
     * @param walletId The walletId to which the deposit should be performed.
     * @param walletDepositDto The necessary information needed to make the deposit.
     */
    void walletDeposit(UUID walletId, WalletDepositDto walletDepositDto);
}
