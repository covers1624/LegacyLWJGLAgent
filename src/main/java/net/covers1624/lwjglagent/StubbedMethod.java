package net.covers1624.lwjglagent;

/**
 * Created by covers1624 on 2/6/24.
 */
public class StubbedMethod extends RuntimeException {

    public StubbedMethod() {
        super("Method is a stub");
    }

    public StubbedMethod(String message) {
        super(message);
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
