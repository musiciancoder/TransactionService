package main.java;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.great.resource.TransactionResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/hello")
public class ExampleResource {

    private final Logger logger = LoggerFactory.getLogger(TransactionResource.class);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
       logger.info("Saying hello from Quarkus REST");
        return "Hello from Quarkus REST";
    }
}
