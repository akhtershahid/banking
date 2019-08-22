package com.mybank.banking.service;

/**
 * @author sakhter
 */
public interface BankingService {

    /**
     * Create an account
     *
     * @return the created acount id
     */
    Long createAccount();

    /**
     * Depoist the given account in the given account
     * @param accountId
     * @param amount
     */
    void deposit(Long accountId, Long amount);

    /**
     * Transfer balance from the sender to receiver account transactionally
     * @param senderAccountId
     * @param receiverAccountId
     * @param amount
     */
    void transfer(Long senderAccountId, Long receiverAccountId, Long amount);

    /**
     * Get balance of the given account
     * @param accountId
     * @return account balance
     */
    Long getBalance(Long accountId);
}
