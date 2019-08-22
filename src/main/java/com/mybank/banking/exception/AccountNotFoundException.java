package com.mybank.banking.exception;

/**
 * @author sakhter
 */
public class AccountNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -456493565022809294L;

    public AccountNotFoundException(String message) {
        super(message);
    }
}
