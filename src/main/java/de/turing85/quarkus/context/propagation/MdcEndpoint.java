package de.turing85.quarkus.context.propagation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("")
public class MdcEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MdcEndpoint.class);

    @GET
    @Path("{mdcKey}")
    @Produces(MediaType.TEXT_PLAIN)
    public String foo(@PathParam("mdcKey") String mdcKey) {
        String mdcValue = MDC.get(mdcKey);
        LOGGER.info("Key \"{}\" has value \"{}\" in MDC", mdcKey, mdcValue);
        return mdcValue;
    }
}