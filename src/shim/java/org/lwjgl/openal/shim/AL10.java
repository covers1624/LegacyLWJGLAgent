package org.lwjgl.openal.shim;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import net.covers1624.lwjglagent.shim.Shim;

@Shim
public class AL10 extends org.lwjgl.openal.AL10 {

    public static void alGetInteger(int pname, IntBuffer data) {
        alGetIntegerv(pname, data);
    }

    public static void alGetFloat(int pname, FloatBuffer data) {
        alGetFloatv(pname, data);
    }

    public static void alGetDouble(int pname, DoubleBuffer data) {
        alGetDoublev(pname, data);
    }

    public static boolean alIsExtensionPresent(String fname) {
        return alIsExtensionPresent((CharSequence) fname);
    }

    public static int alGetEnumValue(String ename) {
        return alGetEnumValue((CharSequence) ename);
    }

    public static void alListener(int pname, FloatBuffer value) {
        alListenerfv(pname, value);
    }

    public static void alGetListener(int pname, FloatBuffer floatdata) {
        alGetListenerf(pname, floatdata);
    }

    public static void alSource(int source, int pname, FloatBuffer value) {
        alSourcefv(source, pname, value);
    }

    public static void alGetSource(int source, int pname, FloatBuffer floatdata) {
        alGetSourcef(source, pname, floatdata);
    }

    public static void alSourcePlay(IntBuffer sources) {
        alSourcePlayv(sources);
    }

    public static void alSourcePause(IntBuffer sources) {
        alSourcePausev(sources);
    }

    public static void alSourceStop(IntBuffer sources) {
        alSourceStopv(sources);
    }

    public static void alSourceRewind(IntBuffer sources) {
        alSourceRewindv(sources);
    }
}
