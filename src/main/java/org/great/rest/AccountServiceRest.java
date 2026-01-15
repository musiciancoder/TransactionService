package org.great.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.great.dto.CreditRequest;
import org.great.dto.ReleaseRequest;
import org.great.dto.ReserveRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.great.resource.TransactionResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RegisterRestClient(configKey = "account-service")
@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AccountServiceRest {

    @POST
    @Path("/reserve")
    void reserve(ReserveRequest req, @HeaderParam("X-Correlation-Id") String correlationId);

    @POST @Path("/credit")
    void credit(CreditRequest req, @HeaderParam("X-Correlation-Id") String correlationId);

    @POST @Path("/release")
    void release(ReleaseRequest req, @HeaderParam("X-Correlation-Id") String correlationId);
}
