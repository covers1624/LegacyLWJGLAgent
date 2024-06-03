package net.covers1624.lwjglagent.logging;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by covers1624 on 2/6/24.
 */
public class ReallySimpleLoggerFactory implements ILoggerFactory {

    private static final Map<String, Integer> LOG_LEVELS = new HashMap<String, Integer>() {
        {
            put("trace", ReallySimpleLogger.LOG_LEVEL_TRACE);
            put("debug", ReallySimpleLogger.LOG_LEVEL_DEBUG);
            put("info", ReallySimpleLogger.LOG_LEVEL_INFO);
            put("warn", ReallySimpleLogger.LOG_LEVEL_WARN);
            put("error", ReallySimpleLogger.LOG_LEVEL_ERROR);
        }
    };

    private static final Integer SELECTED_LOG_LEVEL = LOG_LEVELS.getOrDefault(
            System.getProperty("net.creeperhost.wailt.log_level", "info").toLowerCase(Locale.ROOT),
            ReallySimpleLogger.LOG_LEVEL_INFO
    );

    private final Map<String, ReallySimpleLogger> loggerMap = new ConcurrentHashMap<>();

    @Override
    public Logger getLogger(String name) {
        return loggerMap.computeIfAbsent(name, e -> new ReallySimpleLogger(e, SELECTED_LOG_LEVEL));
    }
}
