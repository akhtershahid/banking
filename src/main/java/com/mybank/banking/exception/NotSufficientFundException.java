package com.mybank.banking.exception;

/**
 * @author sakhter
 */
public class NotSufficientFundException extends RuntimeException {

    private static final long serialVersionUID = -1202975000098442229L;

    public NotSufficientFundException(String message) {
        super(message);
    }
}
