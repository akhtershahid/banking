package com.mybank.banking.provider;

import com.mybank.banking.exception.AccountNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author sakhter
 */
@Provider
public class AccountNotFoundMapper implements ExceptionMapper<AccountNotFoundException> {
    @Override
    public Response toResponse(AccountNotFoundException e) {
        return Response.status(404).entity(e.getMessage()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
