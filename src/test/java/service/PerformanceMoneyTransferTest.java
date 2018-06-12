package service;

import dao.DataRepository;
import dao.InMemoryDataRepository;
import model.Wallet;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.TestHelper;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class PerformanceMoneyTransferTest {

    @BeforeClass
    public static void init() {

        DataRepository dataRepository = new InMemoryDataRepository();

        sAccountService = new AccountServiceImpl(dataRepository);
        sCustomerService = new CustomerServiceImpl(dataRepository);

        sSourceWalletIdHolder = new ArrayList<>();

        sDestinationCustomerDetails = new HashMap<>();

        IntStream.rangeClosed(1, sNumberOfThreads * sNumberOfDepositsPerThread)
                 .forEach(i -> {

                     UUID customerId = sCustomerService.createNewCustomer(TestHelper.createNewCustomerRequest(
                         String.valueOf(i),
                         String.valueOf(i),
                         String.valueOf(i)));

                     UUID accountId = sAccountService.createNewAccount(
                         TestHelper.createNewAccountRequest(customerId.toString()));

                     UUID walletId = sAccountService.createNewWallet(accountId, "EUR");

                     sSourceWalletIdHolder.add(walletId);

                     sAccountService.walletDeposit(walletId, TestHelper.createWalletDepositRequest(String.valueOf(i)));
                 });

        UUID destinationCustomerId = sCustomerService.createNewCustomer(TestHelper.createNewCustomerRequest(
            String.valueOf("firstName"),
            String.valueOf("lastName"),
            String.valueOf("email")));

        UUID destinationAccountId = sAccountService.createNewAccount(
            TestHelper.createNewAccountRequest(destinationCustomerId.toString()));

        UUID destinationWalletId = sAccountService.createNewWallet(destinationAccountId, "EUR");

        sDestinationCustomerDetails.put("AccountId", destinationAccountId);
        sDestinationCustomerDetails.put("WalletId", destinationWalletId);

    }


    @Test(timeout = 5_000)
    public void autobahnTest() throws InterruptedException {

        CountDownLatch cb = new CountDownLatch(sNumberOfThreads);

        UUID destinationAccountId = sDestinationCustomerDetails.get("AccountId");
        UUID destinationWalletId = sDestinationCustomerDetails.get("WalletId");


        IntStream.range(0, sNumberOfThreads)
            .mapToObj(i -> sSourceWalletIdHolder.subList(
                i * sNumberOfDepositsPerThread,
                (i * sNumberOfDepositsPerThread) + sNumberOfDepositsPerThread))
            .map(walletIds ->
                     new Thread(() -> {
                        walletIds.forEach(walletId ->

                            sAccountService.transferFundsBetweenAccounts(
                                TestHelper.createTransferMoneyBetweenAccountsRequest(
                                    walletId.toString(),
                                    destinationWalletId.toString(),
                                    "1.0")));
                      cb.countDown();
                     })
            )
            .forEach(Thread::start);

        cb.await();

        int totalMoneyTransferred = sNumberOfThreads * sNumberOfDepositsPerThread;

        Wallet destinationWallet = TestHelper.getWalletForAccount(
            sAccountService, destinationAccountId, destinationWalletId);

        Assert.assertEquals("Check that destination wallet has the correct amount of money in it.",
            BigDecimal.valueOf((double) totalMoneyTransferred),
            destinationWallet.getAmount());
    }

    private static volatile Map<String, UUID> sDestinationCustomerDetails;

    private static volatile List<UUID> sSourceWalletIdHolder;
    private static final int sNumberOfDepositsPerThread = 10_000;
    private static final int sNumberOfThreads = 10;
    private static volatile AccountService sAccountService;
    private static volatile CustomerService sCustomerService;
}
