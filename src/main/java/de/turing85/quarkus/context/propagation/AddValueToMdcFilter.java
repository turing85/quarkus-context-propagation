package de.turing85.quarkus.context.propagation;

import io.quarkus.vertx.http.runtime.filters.Filters;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import java.util.HashMap;

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
        Vertx.currentContext().putLocal("mdcMap", new HashMap<>(MDC.getMap()));
        context.next();
        MDC.remove(MDC_KEY);
    }
}
