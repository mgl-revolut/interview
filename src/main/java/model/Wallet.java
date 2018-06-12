package model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public final class Wallet {

    public Wallet(UUID walletId, String currency, BigDecimal amount) {

        this.walletId = walletId;
        this.currency = currency;
        this.amount = amount;
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

    @Override
    public boolean equals(Object that) {

        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        final Wallet wallet = (Wallet) that;
        return Objects.equals(walletId, wallet.walletId) &&
                   Objects.equals(currency, wallet.currency) &&
                   Objects.equals(amount, wallet.amount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(walletId, currency, amount);
    }

    private final UUID walletId;
    private final String currency;
    private final BigDecimal amount;
}
