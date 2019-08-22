package com.mybank.banking.service;

import com.mybank.banking.exception.AccountNotFoundException;
import com.mybank.banking.exception.NotSufficientFundException;
import com.mybank.banking.exception.TransferException;
import com.mybank.banking.service.impl.BankingServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author sakhter
 */
public class BankingServiceTest {
    BankingService bankingService;

    @Before
    public void setUp() {
        bankingService = new BankingServiceImpl();
    }

    @Test
    public void testCreateAccount() {
        BankingServiceImpl bankingService = new BankingServiceImpl();
        Long accountId = bankingService.createAccount();
        assertThat(accountId, is(1L));
    }

    @Test
    public void testTransfer() {
        Long accountId1 = bankingService.createAccount();
        bankingService.deposit(accountId1, 2000L);
        Long accountId2 = bankingService.createAccount();
        bankingService.deposit(accountId2, 2000L);

        bankingService.transfer(accountId1, accountId2, 1005L);

        assertThat(bankingService.getBalance(accountId1), is(995L));
        assertThat(bankingService.getBalance(accountId2), is(3005L));
    }

    @Test
    public void testTransfer_notSufficientBalance_expect_NotSufficientFundException() {
        Long accountId1 = bankingService.createAccount();
        Long accountId2 = bankingService.createAccount();

        NotSufficientFundException e = assertThrows(NotSufficientFundException.class,
                () -> bankingService.transfer(accountId1, accountId2, 1005L));

        assertEquals("Not enough fund available to execute the transfer", e.getMessage());
    }

    @Test
    public void testTransfer_senderAccountDoesntExist_expect_AccountNotFoundException() {
        Long accountId2 = bankingService.createAccount();

        AccountNotFoundException e = assertThrows(AccountNotFoundException.class,
                () -> bankingService.transfer(999L, accountId2, 1005L));

        assertEquals("Sender account not found", e.getMessage());
    }

    @Test
    public void testTransfer_receiversAccountDoesntExist_expect_AccountNotFoundException() {
        Long accountId1 = bankingService.createAccount();

        AccountNotFoundException e = assertThrows(AccountNotFoundException.class,
                () -> bankingService.transfer(accountId1, 999L, 1005L));

        assertEquals("Receiver account not found", e.getMessage());
    }

    @Test
    public void testTransfer_receiverAndSenderAccountSame_expect_TransferException() {
        Long accountId1 = bankingService.createAccount();

        TransferException e = assertThrows(TransferException.class,
                () -> bankingService.transfer(accountId1, accountId1, 1005L));

        assertEquals("Sender and Receiver account not distinct", e.getMessage());
    }

}
