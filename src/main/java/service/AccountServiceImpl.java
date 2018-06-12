package service;

import dao.AccountEntity;
import dao.DataRepository;
import dao.WalletEntity;
import dto.NewAccountDto;
import dto.TransferMoneyBetweenAccountsDto;
import dto.WalletDepositDto;
import model.Account;
import model.Wallet;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {

    public AccountServiceImpl(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
    }

    @Override
    public UUID createNewAccount(NewAccountDto accountDto) {

        UUID customerId = UUID.fromString(accountDto.customerId);

        dataRepository.getCustomerDetailsById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customerId"));

        return dataRepository.createNewAccount(customerId);
    }

    @Override
    public Optional<Account> getAccountById(UUID accountId) {

        return dataRepository.getAccountById(accountId).map(this::translateAccountEntity);
    }

    @Override
    public UUID createNewWallet(UUID accountId, String currency) {

        return dataRepository.createNewWallet(accountId, currency);
    }

    @Override
    public Optional<List<Wallet>> getAllWalletsFromAccount(UUID accountId) {

        return dataRepository.getAllWalletsFromAccount(accountId)
                             .map(walletEntities -> walletEntities.stream()
                                                        .map(this::translateWalletEntity)
                                                        .collect(Collectors.toList()));
    }

    @Override
    public void transferFundsBetweenAccounts(TransferMoneyBetweenAccountsDto transferMoneyBetweenAccountsDto) {

        UUID sourceWalletId = UUID.fromString(transferMoneyBetweenAccountsDto.sourceWalletId);
        UUID destinationWalletId = UUID.fromString(transferMoneyBetweenAccountsDto.destinationWalletId);
        BigDecimal amount = BigDecimal.valueOf(Double.valueOf(transferMoneyBetweenAccountsDto.amount));

        if (sourceWalletId.equals(destinationWalletId)) {

            throw new IllegalArgumentException("The source wallet and the destination wallet are the same.");
        }

        if (amount.equals(BigDecimal.ZERO)) {

            throw new IllegalArgumentException("The amount cannot be zero.");
        }

        dataRepository.transferFundsBetweenAccounts(sourceWalletId, destinationWalletId, amount);
    }

    @Override
    public void walletDeposit(UUID walletId, WalletDepositDto walletDepositDto) {

        BigDecimal amount = BigDecimal.valueOf(Double.valueOf(walletDepositDto.amount));

        dataRepository.walletDeposit(walletId, amount);
    }

    private Account translateAccountEntity(AccountEntity accountEntity) {

        return new Account(accountEntity.getAccountId(), accountEntity.getCustomerId(), accountEntity.getWalletIds());
    }

    private Wallet translateWalletEntity(WalletEntity walletEntity) {

        return new Wallet(walletEntity.getWalletId(), walletEntity.getCurrency(), walletEntity.getAmount());
    }

    private final DataRepository dataRepository;
}
