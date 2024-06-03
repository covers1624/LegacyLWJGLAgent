package net.covers1624.lwjglagent.logging;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

/**
 * Created by covers1624 on 2/6/24.
 */
public class ReallySimpleLoggerProvider implements SLF4JServiceProvider {

    private static final String API_VERSION = "2.0";

    private static final ILoggerFactory LOGGER_FACTORY = new ReallySimpleLoggerFactory();
    private static final IMarkerFactory MARKER_FACTORY = new BasicMarkerFactory();
    private static final MDCAdapter MDC_ADAPTER = new NOPMDCAdapter();

    @Override
    public ILoggerFactory getLoggerFactory() {
        return LOGGER_FACTORY;
    }

    @Override
    public IMarkerFactory getMarkerFactory() {
        return MARKER_FACTORY;
    }

    @Override
    public MDCAdapter getMDCAdapter() {
        return MDC_ADAPTER;
    }

    @Override
    public String getRequestedApiVersion() {
        return API_VERSION;
    }

    @Override
    public void initialize() {
    }
}
