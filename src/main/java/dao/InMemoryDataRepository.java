package dao;
import utils.ThreadUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public final class InMemoryDataRepository implements DataRepository {

    public InMemoryDataRepository() {

        wallets = new ConcurrentHashMap<>();
        accounts = new ConcurrentHashMap<>();
        customers = new ConcurrentHashMap<>();
    }

    @Override
    public UUID createNewCustomer(String firstName, String lastName, String email) {

        UUID newCustomerId = UUID.randomUUID();

        customers.put(newCustomerId, new CustomerEntity(newCustomerId, firstName, lastName, email, new ArrayList<>()));

        return newCustomerId;
    }

    @Override
    public UUID createNewAccount(UUID customerId) {

        UUID newAccountId = UUID.randomUUID();

        accounts.put(newAccountId, new AccountEntity(newAccountId, customerId, new ArrayList<>()));

        return newAccountId;
    }

    @Override
    public UUID createNewWallet(UUID accountId, String currency) {

        UUID newWalletId = UUID.randomUUID();
        List<UUID> accountWallets = getAccountById(accountId)
                                    .orElseThrow(() -> new RuntimeException("Account not found."))
                                    .getWalletIds();

        WalletEntity walletEntity = new WalletEntity(newWalletId, currency, BigDecimal.ZERO);
        wallets.put(newWalletId, walletEntity);
        accountWallets.add(newWalletId);

        return newWalletId;
    }

    @Override
    public Optional<List<WalletEntity>> getAllWalletsFromAccount(UUID accountId) {

        return getAccountById(accountId)
                   .map(account -> account.getWalletIds()
                                       .stream()
                                       .map(wallets::get)
                                       .collect(Collectors.toList()));
    }

    @Override
    public Optional<AccountEntity> getAccountById(UUID accountId) {

        return Optional.ofNullable(accounts.get(accountId));
    }

    @Override
    public Optional<CustomerEntity> getCustomerDetailsById(UUID customerId) {

        return Optional.ofNullable(customers.get(customerId));
    }

    @Override
    public void transferFundsBetweenAccounts(UUID sourceWalletId,
                                             UUID destinationWalletId,
                                             BigDecimal amountToTransfer) {

        WalletEntity sourceWallet = wallets.get(sourceWalletId);
        WalletEntity destinationWallet = wallets.get(destinationWalletId);
        boolean unableToAcquireWalletLocks;
        int i = 0;

        do {
            // If the two wallet currencies are not the same then let's pretend that we have a rate service
            // and the rate result is: DestinationCurrencyAmount = SourceCurrencyAmount * 1.2

            BigDecimal finalAmount = sourceWallet.getCurrency().equals(destinationWallet.getCurrency())
                               ? amountToTransfer
                               : amountToTransfer.multiply(BigDecimal.valueOf(1.2));

            unableToAcquireWalletLocks = !lockWalletsAndExecute(
                sourceWallet,
                destinationWallet,
                (fromWallet, toWallet) -> {

                    if (fromWallet.getAmount().compareTo(finalAmount) >= 0) {

                        fromWallet.setAmount(fromWallet.getAmount().subtract(amountToTransfer));
                        toWallet.setAmount(toWallet.getAmount().add(amountToTransfer));
                    } else {

                        throw new RuntimeException("Insufficient funds.");
                    }
                });

            if (unableToAcquireWalletLocks) {

                ThreadUtils.sleepRandomInterval(10, 40);
            }

        } while (unableToAcquireWalletLocks && ++i <= sMaxNumberOfRetriesToExecuteWalletTransaction);
    }

    @Override
    public void walletDeposit(UUID walletId, BigDecimal amount) {

        WalletEntity walletEntity = Optional.ofNullable(wallets.get(walletId))
                                        .orElseThrow(() -> new RuntimeException("Wallet not found."));

        walletEntity.setAmount(walletEntity.getAmount().add(amount));
    }

    private boolean lockWalletsAndExecute(WalletEntity sourceWallet,
                                          WalletEntity destinationWallet,
                                          BiConsumer<WalletEntity, WalletEntity> transaction) {

        boolean sourceWalletLocked = false;
        boolean destinationWalletLocked = false;

        try {
            sourceWalletLocked = sourceWallet.getWalletLock().tryLock();

            if (!sourceWalletLocked) {

                return false;
            }

            destinationWalletLocked = destinationWallet.getWalletLock().tryLock();

            if (!destinationWalletLocked) {

                return false;
            }

            transaction.accept(sourceWallet, destinationWallet);

            return true;

        } finally {

            if (sourceWalletLocked) {
                sourceWallet.getWalletLock().unlock();
            }

            if (destinationWalletLocked) {

                destinationWallet.getWalletLock().unlock();
            }
        }
    }

    private final Map<UUID, WalletEntity> wallets;
    private final Map<UUID, AccountEntity> accounts;
    private final Map<UUID, CustomerEntity> customers;

    private static final int sMaxNumberOfRetriesToExecuteWalletTransaction = 3;
}
