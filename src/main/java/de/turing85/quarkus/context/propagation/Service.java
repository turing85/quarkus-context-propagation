package de.turing85.quarkus.context.propagation;

import org.jboss.logmanager.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);

    public String getValue(String key) {
        final String value = MDC.get(key);
        LOGGER.info("MDC with key \"{}\" has value \"{}\"", key, value);
        return value;
    }
}
