package service;

import dao.DataRepository;
import dao.InMemoryDataRepository;
import model.Account;
import model.Wallet;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.TestHelper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class AccountServiceTest {

    @BeforeClass
    public static void init() {
        DataRepository dataRepository = new InMemoryDataRepository();
        sCustomerService = new CustomerServiceImpl(dataRepository);
        sAccountService = new AccountServiceImpl(dataRepository);
    }

    @Test
    public void createMultipleAccountsToMultipleCustomersSuccessfully() {

        IntStream
            .range(0, 20)
            .forEach(i -> {

                 UUID customerId = sCustomerService.createNewCustomer(
                     TestHelper.createNewCustomerRequest("", "", ""));

                 UUID accountId = sAccountService.createNewAccount(
                     TestHelper.createNewAccountRequest(customerId.toString()));

                 Account account = sAccountService.getAccountById(accountId)
                                       .orElseThrow(() -> new RuntimeException("Account is missing."));

                 Assert.assertEquals("Verify that a newly created account is properly " +
                                              "assigned to appropriate customer.",
                     customerId, account.getCustomerId());
             });
    }

    @Test
    public void verifyThatNewlyCreatedAccountsHaveNoWallets() {

        UUID customerId = sCustomerService.createNewCustomer(
            TestHelper.createNewCustomerRequest("", "", ""));

        UUID accountId = sAccountService.createNewAccount(TestHelper.createNewAccountRequest(customerId.toString()));

        Account account = sAccountService.getAccountById(accountId)
                                      .orElseThrow(() -> new RuntimeException("Account is missing."));

        Assert.assertEquals("Verify that a newly created account has no wallets.",
                    0, account.getWalletIds().size());
    }

    @Test
    public void createNewWalletToAccountSuccessfully() {

        UUID customerId = sCustomerService.createNewCustomer(
            TestHelper.createNewCustomerRequest("", "", ""));

        UUID accountId = sAccountService.createNewAccount(TestHelper.createNewAccountRequest(customerId.toString()));

        UUID walletId = sAccountService.createNewWallet(accountId, "EUR");

        Account account = sAccountService.getAccountById(accountId)
                              .orElseThrow(() -> new RuntimeException("Account is missing."));

        Assert.assertEquals("Verify that account contains wallet.",
            walletId, account.getWalletIds().get(0));

        List<Wallet> walletsRetrieved = sAccountService.getAllWalletsFromAccount(accountId)
            .orElseThrow(() -> new RuntimeException("Wallet is missing."));

        Assert.assertEquals("Verify that account contains exactly 1 wallet",
            1, walletsRetrieved.size());

        Wallet wallet = walletsRetrieved.get(0);

        Assert.assertEquals("Verify that walletId is correct.", walletId, wallet.getWalletId());
        Assert.assertEquals("Verify that currency is correct.", "EUR", wallet.getCurrency());
        Assert.assertEquals("Verify that amount is zero.", BigDecimal.ZERO, wallet.getAmount());
    }

    @Test
    public void makeDepositsToWalletSuccessfully() {

        UUID customerId = sCustomerService.createNewCustomer(
            TestHelper.createNewCustomerRequest("", "", ""));

        UUID accountId = sAccountService.createNewAccount(TestHelper.createNewAccountRequest(customerId.toString()));

        UUID walletId = sAccountService.createNewWallet(accountId, "EUR");

        sAccountService.walletDeposit(walletId, TestHelper.createWalletDepositRequest("124.45"));

        Wallet wallet1 = TestHelper.getWalletForAccount(sAccountService, accountId, walletId);

        Assert.assertEquals("Verify wallet amount.", new BigDecimal("124.45"), wallet1.getAmount());

        sAccountService.walletDeposit(walletId, TestHelper.createWalletDepositRequest("125.55"));

        Wallet wallet2 = TestHelper.getWalletForAccount(sAccountService, accountId, walletId);

        Assert.assertEquals("Verify wallet amount.", new BigDecimal("250.00"), wallet2.getAmount());

        sAccountService.walletDeposit(walletId, TestHelper.createWalletDepositRequest("-125.00"));

        Wallet wallet3 = TestHelper.getWalletForAccount(sAccountService, accountId, walletId);

        Assert.assertEquals("Verify wallet amount.", new BigDecimal("125.00"), wallet3.getAmount());
    }

    @Test
    public void makeTransferBetweenWalletsSuccessfully() {

        UUID customerId1 = sCustomerService.createNewCustomer(
            TestHelper.createNewCustomerRequest("", "", ""));

        UUID accountId1 = sAccountService.createNewAccount(TestHelper.createNewAccountRequest(customerId1.toString()));
        UUID walletId1 = sAccountService.createNewWallet(accountId1, "EUR");
        sAccountService.walletDeposit(walletId1, TestHelper.createWalletDepositRequest("85.70"));

        UUID customerId2 = sCustomerService.createNewCustomer(
            TestHelper.createNewCustomerRequest("", "", ""));

        UUID accountId2 = sAccountService.createNewAccount(TestHelper.createNewAccountRequest(customerId2.toString()));
        UUID walletId2 = sAccountService.createNewWallet(accountId2, "EUR");
        sAccountService.walletDeposit(walletId2, TestHelper.createWalletDepositRequest("5.00"));

        sAccountService.transferFundsBetweenAccounts(
            TestHelper.createTransferMoneyBetweenAccountsRequest(walletId1.toString(), walletId2.toString(), "35.50"));

        Wallet wallet1 = TestHelper.getWalletForAccount(sAccountService, accountId1, walletId1);
        Wallet wallet2 = TestHelper.getWalletForAccount(sAccountService, accountId2, walletId2);

        Assert.assertEquals("Verify wallet1 amount.", new BigDecimal("50.2"), wallet1.getAmount());
        Assert.assertEquals("Verify wallet2 amount.", new BigDecimal("40.5"), wallet2.getAmount());
    }

    @Test
    public void attemptToTransferMoneyWithInsufficientFunds() {

        UUID customerId1 = sCustomerService.createNewCustomer(
            TestHelper.createNewCustomerRequest("", "", ""));

        UUID accountId1 = sAccountService.createNewAccount(TestHelper.createNewAccountRequest(customerId1.toString()));
        UUID walletId1 = sAccountService.createNewWallet(accountId1, "EUR");
        sAccountService.walletDeposit(walletId1, TestHelper.createWalletDepositRequest("100.00"));

        UUID customerId2 = sCustomerService.createNewCustomer(
            TestHelper.createNewCustomerRequest("", "", ""));

        UUID accountId2 = sAccountService.createNewAccount(TestHelper.createNewAccountRequest(customerId2.toString()));
        UUID walletId2 = sAccountService.createNewWallet(accountId2, "EUR");
        sAccountService.walletDeposit(walletId2, TestHelper.createWalletDepositRequest("200.00"));

        try {
            sAccountService.transferFundsBetweenAccounts(
                TestHelper.createTransferMoneyBetweenAccountsRequest(walletId1.toString(), walletId2.toString(), "101.00"));

        } catch (Exception e) {
            // Suppress exception
        }

        Wallet wallet1 = TestHelper.getWalletForAccount(sAccountService, accountId1, walletId1);
        Wallet wallet2 = TestHelper.getWalletForAccount(sAccountService, accountId2, walletId2);

        Assert.assertEquals("Verify wallet1 amount.", new BigDecimal("100.0"), wallet1.getAmount());
        Assert.assertEquals("Verify wallet2 amount.", new BigDecimal("200.0"), wallet2.getAmount());
    }

    private static AccountService sAccountService;
    private static CustomerService sCustomerService;
}
