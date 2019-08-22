package com.mybank.banking.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.JsonSnakeCase;

/**
 *  Account created Representation class
 * @author sakhter
 */
@JsonSnakeCase
public class AccountId {

    private Long accountId;

    public AccountId() {
    }

    @JsonCreator
    public AccountId(@JsonProperty Long accountId) {
        this.accountId = accountId;
    }

    @JsonProperty
    public Long getAccountId() {
        return accountId;
    }


    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
