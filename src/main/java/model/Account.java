package model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Account {

    public Account(UUID accountId, UUID customerId, List<UUID> walletIds) {

        this.accountId = accountId;
        this.customerId = customerId;
        this.walletIds = walletIds;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public List<UUID> getWalletIds() {
        return walletIds;
    }

    @Override
    public boolean equals(Object that) {

        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        final Account account = (Account) that;
        return Objects.equals(accountId, account.accountId) &&
                   Objects.equals(customerId, account.customerId) &&
                   Objects.equals(walletIds, account.walletIds);
    }

    @Override
    public int hashCode() {

        return Objects.hash(accountId, customerId, walletIds);
    }

    private final UUID accountId;
    private final UUID customerId;
    private final List<UUID> walletIds;
}
