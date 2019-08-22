package com.mybank.banking.exception;

/**
 * @author sakhter
 */
public class TransferException extends RuntimeException {

    private static final long serialVersionUID = 2527022792155357732L;

    public TransferException(String message) {
        super(message);
    }
}
