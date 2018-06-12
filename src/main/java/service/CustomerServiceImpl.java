package service;

import dao.CustomerEntity;
import dao.DataRepository;
import dto.NewCustomerDto;
import model.Customer;
import java.util.Optional;
import java.util.UUID;

public class CustomerServiceImpl implements CustomerService {

    public CustomerServiceImpl(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
    }

    @Override
    public UUID createNewCustomer(NewCustomerDto customerDto) {

        return dataRepository.createNewCustomer(customerDto.firstName, customerDto.lastName, customerDto.email);
    }

    @Override
    public Optional<Customer> getAccountById(UUID customerId) {

        return dataRepository.getCustomerDetailsById(customerId).map(this::translateCustomerEntity);
    }

    private Customer translateCustomerEntity(CustomerEntity customerEntity) {

        return new Customer(customerEntity.getCustomerId(),
            customerEntity.getFirstName(),
            customerEntity.getLastName(),
            customerEntity.getEmail(),
            customerEntity.getAccountIds());
    }

    private final DataRepository dataRepository;
}
