package service;

import dao.InMemoryDataRepository;
import model.Customer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.TestHelper;

import java.util.UUID;

public class CustomerServiceTest {

    @BeforeClass
    public static void init() {

        sCustomerService = new CustomerServiceImpl(new InMemoryDataRepository());
    }

    @Test
    public void createNewCustomerSuccessfully() {

        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "email@email.com";

        UUID customerId = sCustomerService.createNewCustomer(TestHelper.createNewCustomerRequest(firstName, lastName, email));

        Customer customer = sCustomerService.getAccountById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer is missing."));

        Assert.assertEquals("Verify that customer firstName is correct.", firstName, customer.getFirstName());
        Assert.assertEquals("Verify that customer lastName is correct.", lastName, customer.getLastName());
        Assert.assertEquals("Verify that customer email is correct.", email, customer.getEmail());
    }

    private static CustomerService sCustomerService;
}
