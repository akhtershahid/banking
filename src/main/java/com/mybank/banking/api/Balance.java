package com.mybank.banking.api;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Balance representation class. Represents the balance of an account in penny
 * @author sakhter
 */
public class Balance {
    private Long amount;

    public Balance() {}

    @JsonCreator
    public Balance(Long amount) {
        this.amount = amount;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
