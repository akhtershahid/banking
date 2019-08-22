package com.mybank.banking.service.impl;

import com.mybank.banking.exception.AccountNotFoundException;
import com.mybank.banking.exception.NotSufficientFundException;
import com.mybank.banking.exception.TransferException;
import com.mybank.banking.model.Account;
import com.mybank.banking.service.BankingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

/**
 * @author sakhter
 */

@Singleton
public class BankingServiceImpl implements BankingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankingServiceImpl.class);

    private Map<Long, Account> accounts = new ConcurrentHashMap<>();

    private AtomicLong idGenerator = new AtomicLong();

    @Override
    public Long createAccount(){
        Account account = new Account(idGenerator.incrementAndGet());
        accounts.put(account.getAccountId(), account);
        LOGGER.info("Account created wth AccountId : {}", account.getAccountId());
        return account.getAccountId();
    }

    @Override
    public void deposit(Long accountId, Long amount) {
        Account account = findAccount(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        LOGGER.info("Deposited {} to account {}", amount, accountId);
        account.deposit(amount);
    }

    @Override
    public void transfer(Long senderAccountId, Long receiverAccountId, Long amount) {
        if(senderAccountId == receiverAccountId) {
            throw new TransferException("Sender and Receiver account not distinct");
        }
        Account senderAccount = findAccount(senderAccountId).orElseThrow(() -> new AccountNotFoundException("Sender account not found"));
        Account receiverAccount = findAccount(receiverAccountId).orElseThrow(() -> new AccountNotFoundException("Receiver account not found"));

        doTransfer(senderAccount, receiverAccount, (from, to) -> {
            if(from.getBalance().compareTo(amount) < 0) {
                LOGGER.error("There is not enough fund in account {} for transfer", senderAccount.getAccountId());
                throw new NotSufficientFundException("Not enough fund available to execute the transfer");
            } else {
                senderAccount.withDraw(amount);
                receiverAccount.deposit(amount);
                LOGGER.info("Transfer completed successfully");
            }
        });
    }

    @Override
    public Long getBalance(Long accountId) {
        Optional<Account> accountOptional = findAccount(accountId);
        Account account = accountOptional.orElseThrow(() -> new AccountNotFoundException("Account not found"));
        return account.getBalance();
    }

    private Optional<Account> findAccount(Long accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }

    /**
     * Makes a transfer from teh sender to the receiver account transactionally by acquiring locks
     * on both the accounts.
     * To avoid deadlock acquires lock on the accounts in the ascending order of the accountId
     * Releases locks in the reverse order
     */
    private void doTransfer(Account sender, Account receiver, BiConsumer<Account, Account> transfer) {
        Account lock1;
        Account lock2;
        if(sender.getAccountId() > receiver.getAccountId()) {
            lock1 = sender;
            lock2 = receiver;
        } else {
            lock1 = receiver;
            lock2 = sender;
        }

        lock1.lock();
        try {
            lock2.lock();
            try {
                transfer.accept(sender, receiver);
            } finally{
                lock2.unlock();
            }
        } finally {
            lock1.unlock();
        }
    }
}
