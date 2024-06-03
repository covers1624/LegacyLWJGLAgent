package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.shim.Shim;

import java.nio.IntBuffer;

@Shim
public class GL42 extends org.lwjgl.opengl.GL42 {

    public static void glGetActiveAtomicCounterBuffer(int program, int bufferIndex, int pname, IntBuffer params) {
        glGetActiveAtomicCounterBufferiv(program, bufferIndex, pname, params);
    }

    public static int glGetActiveAtomicCounterBuffer(int program, int bufferIndex, int pname) {
        return glGetActiveAtomicCounterBufferi(program, bufferIndex, pname);
    }

    public static void glGetInternalformat(int target, int internalformat, int pname, IntBuffer params) {
        glGetInternalformativ(target, internalformat, pname, params);
    }

    public static int glGetInternalformat(int target, int internalformat, int pname) {
        return glGetInternalformati(target, internalformat, pname);
    }
}
