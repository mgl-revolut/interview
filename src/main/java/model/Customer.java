package model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Customer {

    public Customer(UUID customerId, String firstName, String lastName, String email, List<UUID> accountIds) {

        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accountIds = accountIds;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<UUID> getAccountIds() {
        return accountIds;
    }

    @Override
    public boolean equals(Object that) {

        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        final Customer customer = (Customer) that;
        return Objects.equals(customerId, customer.customerId) &&
                   Objects.equals(firstName, customer.firstName) &&
                   Objects.equals(lastName, customer.lastName) &&
                   Objects.equals(email, customer.email) &&
                   Objects.equals(accountIds, customer.accountIds);
    }

    @Override
    public int hashCode() {

        return Objects.hash(customerId, firstName, lastName, email, accountIds);
    }

    private final UUID customerId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final List<UUID> accountIds;
}
