package org.great.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.great.dto.TransactionRequest;
import org.great.repository.TransactionRepository;
import org.great.saga.SagaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Path("/transfers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private final TransactionRepository repo;
    private final SagaService saga;

    private final Logger logger = LoggerFactory.getLogger(TransactionResource.class);

    public TransactionResource(TransactionRepository repo, SagaService saga) {
        this.repo = repo; this.saga = saga;
    }

    @POST
    @Path("/start")
    @RolesAllowed({"ROLE_USER","ROLE_ADMIN"})
    @Transactional
    public Response transfer(@Context HttpHeaders headers, @Valid TransactionRequest req) {
        logger.info("Transfer started from {} to {} amount {}",
                req.sourceAccount(), req.targetAccount(), req.amount());
        if (req.sourceAccount().equals(req.targetAccount())) throw new WebApplicationException("Same account", 400);
        if (req.amount() == null || req.amount().signum() <= 0) throw new WebApplicationException("Invalid amount", 400);

        var correlationId = headers.getHeaderString("X-Correlation-Id");
        if (correlationId == null || correlationId.isBlank()) correlationId = UUID.randomUUID().toString();

        var t = saga.start(req, correlationId);
        saga.execute(t);

        return Response.ok(t).header("X-Correlation-Id", correlationId).build();
    }

    @GET @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        return repo.findByIdOptional(id).map(Response::ok).orElse(Response.status(404)).build();
    }

    @GET @Path("/by-account/{number}")
    public Response list(@PathParam("number") String number) {
        return Response.ok(repo.listByAccount(number)).build();
    }
}
