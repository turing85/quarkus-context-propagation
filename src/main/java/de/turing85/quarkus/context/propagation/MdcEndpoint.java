package de.turing85.quarkus.context.propagation;

import io.vertx.core.Vertx;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Optional;

@Path("")
public class MdcEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MdcEndpoint.class);

    @GET
    @Path("{mdcKey}")
    @Produces(MediaType.TEXT_PLAIN)
    @SuppressWarnings("unchecked")
    public String foo(@PathParam("mdcKey") String mdcKey) {
        ((Map<String, String>) Vertx.currentContext().getLocal("mdcMap"))
                .forEach(MDC::put);
        String mdcValue = Optional.ofNullable(MDC.get(mdcKey)).map(Object::toString).orElse("null");
        LOGGER.info("Key \"{}\" has value \"{}\" in MDC", mdcKey, mdcValue);
        return mdcValue;
    }
}