package de.turing85.quarkus.context.propagation;

import io.quarkus.vertx.http.runtime.filters.Filters;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

@Dependent
public class AddValueToMdcFilter {
    public static final Logger LOGGER = LoggerFactory.getLogger(AddValueToMdcFilter.class);
    public static final String MDC_KEY = "FOO";
    public static final String MDC_VALUE = "foo";

    void registerFilter(@Observes Filters filters) {
        filters.register(this::addValueToMdcAndProceed, 10);
    }

    void addValueToMdcAndProceed(RoutingContext context) {
        MDC.put(MDC_KEY, MDC_VALUE);
        LOGGER.info("Key \"{}\" with value \"{}\" is now in MDC", MDC_KEY, MDC.get(MDC_KEY));

        context.next();
    }
}
