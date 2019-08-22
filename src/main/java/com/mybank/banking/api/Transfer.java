package com.mybank.banking.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.JsonSnakeCase;

import javax.validation.constraints.NotNull;

/**
 * Represents a transfer request from the sender to receiver's account for the given amount.
 * @author sakhter
 */
@JsonSnakeCase
public class Transfer {

    @NotNull
    private Long senderAccount;

    @NotNull
    private Long receiverAccount;

    @NotNull
    private Long amount;

    public Transfer() {
    }

    @JsonCreator
    public Transfer(@JsonProperty("sender_account") Long senderAccount,
            @JsonProperty("receiver_account") Long receiverAccount,
            @JsonProperty("amount") Long amount) {
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.amount = amount;
    }

    public Long getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(Long senderAccount) {
        this.senderAccount = senderAccount;
    }

    public Long getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(Long receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
