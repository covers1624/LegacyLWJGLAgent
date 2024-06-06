package net.covers1624.lwjglagent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by covers1624 on 2/6/24.
 */
public class StubbedMethod extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(StubbedMethod.class);

    public StubbedMethod() {
        this("Method is a stub");
    }

    public StubbedMethod(String message) {
        super(message);
        LOGGER.error("Stubbed method called.", this);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + ": " + getCaller();
    }

    public String getCaller() {
        StackTraceElement[] stackTrace = getStackTrace();
        if (stackTrace.length == 0) return "UNKNOWN??";
        return stackTrace[0].toString();
    }
}
