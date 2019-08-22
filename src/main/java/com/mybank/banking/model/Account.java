package com.mybank.banking.model;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Balance is stored in penny(The lowest denomination of a standard currency.)
 *
 * @author sakhter
 */
public class Account {

    private Long accountId;

    private Long balance;

    private ReentrantLock lock = new ReentrantLock();

    public Account(Long accountId) {
        this.accountId = accountId;
        this.balance = 0L;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Long getBalance() {
        return balance;
    }

    public void deposit(Long amount) {
        this.balance = this.balance + amount;
    }

    public void withDraw(Long amount) {
        this.balance = this.balance - amount;
    }

    public void  lock() {
        this.lock.lock();
    }

    public void unlock() {
        this.lock.unlock();
    }
}
