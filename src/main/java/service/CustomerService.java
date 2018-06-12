package service;

import dto.NewCustomerDto;
import model.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    /**
     * Creates a new customer.
     * @param customerDto The necessary information needed to create a new customer record.
     * @return The customerId is returned, upon successful customer creation.
     */
    UUID createNewCustomer(NewCustomerDto customerDto);

    /**
     * Retrieves a customer record based on its id.
     * @param customerId The customer id..
     * @return Returns the customer record if such exists, or {@link java.util.Optional#empty()} otherwise.
     */
    Optional<Customer> getAccountById(UUID customerId);
}
