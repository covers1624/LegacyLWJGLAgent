package net.covers1624.lwjglagent.logging;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.AbstractLogger;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by covers1624 on 2/6/24.
 */
public class ReallySimpleLogger extends AbstractLogger {

    public static final int LOG_LEVEL_TRACE = LocationAwareLogger.TRACE_INT;
    public static final int LOG_LEVEL_DEBUG = LocationAwareLogger.DEBUG_INT;
    public static final int LOG_LEVEL_INFO = LocationAwareLogger.INFO_INT;
    public static final int LOG_LEVEL_WARN = LocationAwareLogger.WARN_INT;
    public static final int LOG_LEVEL_ERROR = LocationAwareLogger.ERROR_INT;

    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final PrintStream SYS_OUT = System.out;

    private final String name;
    private final int logLevel;

    ReallySimpleLogger(String name, int logLevel) {
        this.name = name;
        this.logLevel = logLevel;
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String messagePattern, Object[] arguments, @Nullable Throwable throwable) {
        if (level.toInt() < logLevel) return;

        StringBuilder builder = new StringBuilder();
        builder.append('[').append(TIME_FORMAT.format(new Date())).append("] ");
        builder.append('[').append(Thread.currentThread().getName()).append('/').append(level).append("] ");
        builder.append("[").append(name).append("]: ");
        builder.append(MessageFormatter.arrayFormat(messagePattern, arguments).getMessage());

        synchronized (SYS_OUT) {
            SYS_OUT.println(builder);
            if (throwable != null) {
                throwable.printStackTrace(SYS_OUT);
            }
            SYS_OUT.flush();
        }
    }

    //@formatter:off
    @Override public boolean isTraceEnabled(Marker marker) { return isTraceEnabled(); }
    @Override public boolean isDebugEnabled(Marker marker) { return isDebugEnabled(); }
    @Override public boolean isInfoEnabled(Marker marker) { return isInfoEnabled(); }
    @Override public boolean isWarnEnabled(Marker marker) { return isWarnEnabled(); }
    @Override public boolean isErrorEnabled(Marker marker) { return isErrorEnabled(); }
    @Override public boolean isTraceEnabled() { return LOG_LEVEL_TRACE >= logLevel; }
    @Override public boolean isDebugEnabled() { return LOG_LEVEL_DEBUG >= logLevel; }
    @Override public boolean isInfoEnabled() { return LOG_LEVEL_INFO >= logLevel; }
    @Override public boolean isWarnEnabled() { return LOG_LEVEL_WARN >= logLevel; }
    @Override public boolean isErrorEnabled() { return LOG_LEVEL_ERROR >= logLevel; }
    @Override protected String getFullyQualifiedCallerName() { return null; }
    //@formatter:on
}
