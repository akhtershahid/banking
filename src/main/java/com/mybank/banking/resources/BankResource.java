package com.mybank.banking.resources;

import com.mybank.banking.api.AccountId;
import com.mybank.banking.api.Balance;
import com.mybank.banking.service.BankingService;
import io.swagger.annotations.Api;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Provides operations to manage account in Bank
 *
 * @author sakhter
 */

@Api
@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BankResource {

    private BankingService bankingService;

    @Inject
    public BankResource(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    /**
     * Creates an account with Zero balance.
     *
     * @returns the unique account id and {@code 201}
     */
    @POST
    public Response createAccount() {
        Long accountId = bankingService.createAccount();
        return Response.status(Response.Status.CREATED).entity(new AccountId(accountId)).build();
    }

    /**
     * Deposit a money provided in penny to a given account
     * @param accountId The depositor account Id
     * @param amount The amount to be deposited
     * @return {@code 204}, if success. {@code 404}, if account is not found
     */
    @POST
    @Path("{accountId}/{amount}")
    public Response deposit(@PathParam("accountId") @NotNull Long accountId, @PathParam("amount") @NotNull Long amount) {
        bankingService.deposit(accountId, amount);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * Get the balance of a given account
     * @param accountId The account id
     * @return Account balance in penny and {@code 200}, {@code 404}, if account is not found
     */
    @GET
    @Path("{accountId}")
    public Response getBalance(@PathParam("accountId") @NotNull Long accountId) {
        return Response.status(Response.Status.OK).entity(new Balance(bankingService.getBalance(accountId))).build();
    }

}
