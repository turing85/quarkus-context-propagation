package de.turing85.quarkus.context.propagation;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Duration;


@Path("/headers")
public class MdcEndpoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(MdcEndpoint.class);
  public static final Duration DURATION = Duration.ofSeconds(5);

  private final Service service;
  private final String mdcCorrelationId;

  public MdcEndpoint(
      Service service,
      @ConfigProperty(name = "app.config.log.mdc.keys.correlation-id") String mdcCorrelationId) {
    this.service = service;
    this.mdcCorrelationId = mdcCorrelationId;
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Uni<String> foo() {
    return Uni
        .createFrom().item(mdcCorrelationId)
        .onItem()
            .invoke(() -> LOGGER.info("Sleeping for {}", DURATION))
        .onItem()
            .delayIt().by(DURATION)
        .onItem()
            .invoke(() -> LOGGER.info("Waking up"))
        .onItem()
            .transform(service::getValue);
  }
}