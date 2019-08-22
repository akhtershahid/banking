package com.mybank.banking.provider;

import com.mybank.banking.exception.NotSufficientFundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author sakhter
 */
@Provider
public class NotSufficientFundMapper implements ExceptionMapper<NotSufficientFundException> {
    @Override
    public Response toResponse(NotSufficientFundException e) {
        return Response.status(409).entity(e.getMessage()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
