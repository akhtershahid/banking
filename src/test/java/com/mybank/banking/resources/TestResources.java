package com.mybank.banking.resources;

import com.mybank.banking.MyBankConfigguration;
import com.mybank.banking.MyBankingApp;
import com.mybank.banking.api.AccountId;
import com.mybank.banking.api.Balance;
import com.mybank.banking.api.Transfer;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author sakhter
 */
public class TestResources {

    @Rule
    public final DropwizardAppRule<MyBankConfigguration> RULE =
            new DropwizardAppRule<>(MyBankingApp.class, "config.yml");

    private Client client;

    @Before
    public void setupClass() {
        client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");
    }

    @Test
    public void testCreateAccount() {
        //when: create an account
        Response response = client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .request()
                .post(null);

        //then assert http status code 201
        assertEquals(response.getStatus(), 201);
        AccountId account = response.readEntity(AccountId.class);
        //then assert accountId
        assertThat(1L, is(account.getAccountId()));
        //then assert media type returned
        assertThat(response.getMediaType(), is(MediaType.APPLICATION_JSON_TYPE));

    }

    @Test
    public void testDeposit() {
        //Given: a account
        client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .request()
                .post(null);

        // when: depost 2000 cents to accountId 1
        Response response = client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .path("1/2000")
                .request()
                .post(null);

        // then: assert http status code 204
        assertEquals(response.getStatus(), 204);

        // assert the balance in account is 2000 cents
        assertThat(2000L, is((client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .path("1")
                .request()
                .get()).readEntity(Balance.class).getAmount()));
    }

    @Test
    public void testDeposit_AccountNotExist_expect_404() {
        //Given: Account doesn't exist

        //When: deposit money to the acount
        Response response = client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .path("1/2000")
                .request()
                .post(null);

        //Then: expect http status 404
        assertEquals(response.getStatus(), 404);
    }


    @Test
    public void testGetBalance() {
        // Given: An account
        client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .request()
                .post(null);
        // And: deposit 2000 cents to teh account
        client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .path("1/2000")
                .request()
                .post(null);

        //When: get balance
        Response response = client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .path("1")
                .request()
                .get();

        //Then: status code is 200
        assertEquals(response.getStatus(), 200);

        Balance balance = response.readEntity(Balance.class);
        //And: Balance is 2000 cents
        assertThat(2000L, is(balance.getAmount()));
    }

    @Test
    public void testGetBalance_AccountNotExist_expect_404() {
        //Given: Account doesnt exists

        //When: get balance
        Response response = client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .path("1")
                .request()
                .get();

        //Then: status code is 404
        assertEquals(response.getStatus(), 404);
    }

    @Test
    public void testTransfer_SenderAccountNotExits_expect_404() {

        //Given: Sender Account doesn't

        //When: do transfer
        Response response = client.target(
                String.format("http://localhost:%d/mybank/transfer", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(new Transfer(1L, 2L, 1000L), MediaType.APPLICATION_JSON_TYPE));

        //Then status code is 404
        assertEquals(response.getStatus(), 404);
        //And response message is 'Sender account not found'
        assertThat(response.readEntity(String.class), is("Sender account not found"));
    }

    @Test
    public void testTransfer_ReceiverAccountNotExits_expect_404() {
        //Given: Senders account exists and receivers account doesn't exists
        client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .request()
                .post(null);

        //When: do transfer
        Response response = client.target(
                String.format("http://localhost:%d/mybank/transfer", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(new Transfer(1L, 2L, 1000L), MediaType.APPLICATION_JSON_TYPE));

        //Then: status code is 404
        assertEquals(response.getStatus(), 404);
        //And response message is 'Receiver account not found'
        assertThat(response.readEntity(String.class), is("Receiver account not found"));
    }

    @Test
    public void testTransfer_NotSufficientFund_expect_404() {
        //Given: Account 1 as sender having 0 balance
        client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .request()
                .post(null);

        //And: account 2 as freceiver
        client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .request()
                .post(null);

        //when:  Transfer 1000 cents from sender t0 receiver
        Response response = client.target(
                String.format("http://localhost:%d/mybank/transfer", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(new Transfer(1L, 2L, 1000L), MediaType.APPLICATION_JSON_TYPE));

        //Then: status code is 409
        assertEquals(response.getStatus(), 409);
        //And: response message is 'Not enough fund available to execute the transfer'
        assertThat(response.readEntity(String.class), is("Not enough fund available to execute the transfer"));
    }

    @Test
    public void testTransfer() {

        //Given: Account 1 as sender
        client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .request()
                .post(null);

        //And: deposit 2000 cents to sender
        client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .path("1/2000")
                .request()
                .post(null);
        //And: Account 2 as receiver having 0 balance
        client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .request()
                .post(null);

        //When: transfer 1000 cents from sender to receiver
        Response response = client.target(
                String.format("http://localhost:%d/mybank/transfer", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(new Transfer(1L, 2L, 1000L), MediaType.APPLICATION_JSON_TYPE));

        //Then: status code is 204
        assertEquals(response.getStatus(), 204);

        //And: balance in sender account is 1000 cents
        assertThat(1000L, is((client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .path("1")
                .request()
                .get()).readEntity(Balance.class).getAmount()));

        //And: balance in receiver account is 1000 cents
        assertThat(1000L, is((client.target(
                String.format("http://localhost:%d/mybank/account", RULE.getLocalPort()))
                .path("2")
                .request()
                .get()).readEntity(Balance.class).getAmount()));
    }

    @Test
    public void testTransfer_senderAccountIdIsNull_expect_422() {
        //When: Transfer from sender having accountId null
        Response response = client.target(
                String.format("http://localhost:%d/mybank/transfer", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(new Transfer(null, 2L, 1000L), MediaType.APPLICATION_JSON_TYPE));

        //Then: status code is 422
        assertEquals(response.getStatus(), 422);
    }

    @Test
    public void testTransfer_receiverAccountIdIsNull_expect_422() {
        //When: Transfer to receiver having accountId null
        Response response = client.target(
                String.format("http://localhost:%d/mybank/transfer", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(new Transfer(1L, null, 1000L), MediaType.APPLICATION_JSON_TYPE));

        //Then: status code is 422
        assertEquals(response.getStatus(), 422);
    }

    @Test
    public void testTransfer_transferAmountIsNull_expect_422() {
        //When: Transfer to receiver having accountId null
        Response response = client.target(
                String.format("http://localhost:%d/mybank/transfer", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(new Transfer(1L, 2L, null), MediaType.APPLICATION_JSON_TYPE));

        //Then: status code is 422
        assertEquals(response.getStatus(), 422);
    }

    @Test
    public void testTransfer_senderAndReceiverAccountSame_expect_404() {
        //When: Transfer to receiver having accountId null
        Response response = client.target(
                String.format("http://localhost:%d/mybank/transfer", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(new Transfer(1L, 2L, null), MediaType.APPLICATION_JSON_TYPE));

        //Then: status code is 422
        assertEquals(response.getStatus(), 422);
    }
}
