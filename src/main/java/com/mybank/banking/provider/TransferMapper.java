package com.mybank.banking.provider;

import com.mybank.banking.exception.TransferException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author sakhter
 */
@Provider
public class TransferMapper implements ExceptionMapper<TransferException> {
    @Override
    public Response toResponse(TransferException e) {
        return Response.status(400).entity(e.getMessage()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
