package core;

import service.AccountService;
import service.CustomerService;

public final class ServiceRegistry {

    public ServiceRegistry(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    private final AccountService accountService;
    private final CustomerService customerService;
}
