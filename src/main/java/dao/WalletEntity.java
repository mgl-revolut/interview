package dao;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WalletEntity {

    public WalletEntity(UUID walletId, String currency, BigDecimal amount) {

        walletLock = new ReentrantLock();
        this.walletId = walletId;
        this.currency = currency;
        this.amount = amount;
    }

    public Lock getWalletLock() {
        return walletLock;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal newAmount) {
        amount = newAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final WalletEntity that = (WalletEntity) o;
        return Objects.equals(walletLock, that.walletLock) &&
                   Objects.equals(walletId, that.walletId) &&
                   Objects.equals(currency, that.currency) &&
                   Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(walletLock, walletId, currency, amount);
    }

    private final Lock walletLock;
    private final UUID walletId;
    private final String currency;
    private volatile BigDecimal amount;
}
