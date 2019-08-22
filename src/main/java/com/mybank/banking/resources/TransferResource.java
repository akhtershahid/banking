package com.mybank.banking.resources;

import com.mybank.banking.api.Transfer;
import com.mybank.banking.service.BankingService;
import io.swagger.annotations.Api;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Endpoint provides transfer of balance between accounts
 *
 * @author sakhter
 */

@Api
@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransferResource {

    private BankingService bankingService;

    @Inject
    public TransferResource(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    /**
     * Transfer given amount in penny from Sender account to Receiver account
     *
     * @param transfer The transfer object
     * @return {@code 204} if success,
     *      {@code 404} if either of the accounts doesn't exists.
     *      {@code 409} if not sufficient balance available in sender account
     *      {@code 422} if transfer object is not valid
     */
    @POST
    public Response transfer(@Valid @NotNull Transfer transfer){
        bankingService.transfer(transfer.getSenderAccount(),
                transfer.getReceiverAccount(),
                transfer.getAmount());
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
