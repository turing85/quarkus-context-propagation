package de.turing85.quarkus.context.propagation;

import io.quarkus.vertx.http.runtime.filters.Filters;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import java.util.Optional;
import java.util.UUID;

@Dependent
public class AddValueToMdcFilter {
  public static final Logger LOGGER = LoggerFactory.getLogger(AddValueToMdcFilter.class);

  void registerFilter(@Observes Filters filters) {
    filters.register(this::addValueToMdcAndProceed, 10);
  }

  private final String headerCorrelationId;
  private final String mdcCorrelationId;

  public AddValueToMdcFilter(
      @ConfigProperty(name = "app.config.http.header.keys.correlation-id") String headerCorrelationId,
      @ConfigProperty(name = "app.config.log.mdc.keys.correlation-id") String mdcCorrelationId) {
    this.headerCorrelationId = headerCorrelationId;
    this.mdcCorrelationId = mdcCorrelationId;
  }

  void addValueToMdcAndProceed(RoutingContext context) {
    String correlationId = Optional.of(context)
        .map(RoutingContext::request)
        .map(request -> request.getHeader(headerCorrelationId))
        .orElseGet(() -> UUID.randomUUID().toString());
    context.request().headers()
        .remove(headerCorrelationId)
        .add(headerCorrelationId, correlationId);
    context.response().headers()
        .remove(headerCorrelationId)
        .add(headerCorrelationId, correlationId);
    MDC.put(mdcCorrelationId, correlationId);
    LOGGER.info("Key \"{}\" with value \"{}\" is now in MDC", mdcCorrelationId, MDC.get(mdcCorrelationId));
    context.next();
  }
}
