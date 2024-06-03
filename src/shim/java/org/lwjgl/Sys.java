package org.lwjgl;

import net.covers1624.lwjglagent.StubbedMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by covers1624 on 2/6/24.
 */
public class Sys {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sys.class);

    public static String getVersion() {
        return Version.getVersion() + " Via LWJGLAgent.";
    }

    public static boolean is64Bit() {
        throw new StubbedMethod();
    }

    // TODO windows?
    public static long getTimerResolution() {
        return 1000;
    }

    public static long getTime() {
        return System.currentTimeMillis();
    }
}
