import api.*;
import core.BaseController;
import dao.InMemoryDataRepository;
import service.AccountServiceImpl;
import service.CustomerServiceImpl;
import core.ServiceRegistry;

import java.util.Arrays;

import static conf.ApplicationConfiguration.sPort;
import static spark.Spark.port;

public final class Application {

    public static void main(String[] args) {

        port(sPort);

        InMemoryDataRepository dataRepository = new InMemoryDataRepository();

        ServiceRegistry serviceRegistry = new ServiceRegistry(
            new AccountServiceImpl(dataRepository),
            new CustomerServiceImpl(dataRepository));

            Arrays.asList(
                new CreateNewCustomer(serviceRegistry),
                new CreateNewAccount(serviceRegistry),
                new GetAccountDetails(serviceRegistry),
                new CreateNewWallet(serviceRegistry),
                new TransferMoneyBetweenAccounts(serviceRegistry),
                new WalletDeposit(serviceRegistry))
             .forEach(BaseController::init);
    }
}
