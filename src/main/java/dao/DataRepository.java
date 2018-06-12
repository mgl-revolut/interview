package dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataRepository {

    UUID createNewCustomer(String firstName, String lastName, String email);

    UUID createNewAccount(UUID accountId);

    UUID createNewWallet(UUID accountId, String currency);

    /**
     * Returns all the wallets that are associated with the given accountId.
     * @param accountId the accountId for whose wallets need to be retrieved
     * @return the list of the wallets if accountId exists, or {@link java.util.Optional#empty()} otherwise
     */
    Optional<List<WalletEntity>> getAllWalletsFromAccount(UUID accountId);

    Optional<AccountEntity> getAccountById(UUID accountId);

    Optional<CustomerEntity> getCustomerDetailsById(UUID customerId);

    void transferFundsBetweenAccounts(UUID sourceAccount, UUID destinationAccount, BigDecimal amount);

    void walletDeposit(UUID walletId, BigDecimal amount);
}
