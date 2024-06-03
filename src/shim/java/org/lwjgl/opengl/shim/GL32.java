package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.StubbedMethod;
import net.covers1624.lwjglagent.shim.Named;
import net.covers1624.lwjglagent.shim.Shim;
import org.lwjgl.opengl.GLSync;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

@Shim
public class GL32 extends org.lwjgl.opengl.GL32 {

    public static void glGetBufferParameter(int target, int pname, LongBuffer params) {
        throw new StubbedMethod();
    }

    public static long glGetBufferParameter(int target, int pname) {
        throw new StubbedMethod();
    }

    public static void glGetMultisample(int pname, int index, FloatBuffer val) {
        glGetMultisamplefv(pname, index, val);
    }

    @Named ("glFenceSync")
    public static GLSync glFenceSync2(int condition, int flags) {
        return new GLSync(glFenceSync(condition, flags));
    }

    public static boolean glIsSync(GLSync sync) {
        return glIsSync(sync.getPointer());
    }

    public static void glDeleteSync(GLSync sync) {
        glDeleteSync(sync.getPointer());
    }

    public static int glClientWaitSync(GLSync sync, int flags, long timeout) {
        return glClientWaitSync(sync.getPointer(), flags, timeout);
    }

    public static void glWaitSync(GLSync sync, int flags, long timeout) {
        glWaitSync(sync.getPointer(), flags, timeout);
    }

    public static void glGetInteger64(int pname, LongBuffer data) {
        glGetInteger64v(pname, data);
    }

    public static void glGetInteger64(int value, int index, LongBuffer data) {
        glGetInteger64i_v(value, index, data);
    }

    public static long glGetInteger64(int value, int index) {
        return glGetIntegeri(value, index);
    }

    public static void glGetSync(GLSync sync, int pname, IntBuffer length, IntBuffer values) {
        glGetSynciv(sync.getPointer(), pname, length, values);
    }

    public static int glGetSync(GLSync sync, int pname) {
        return glGetSynci(sync, pname);
    }

    public static int glGetSynci(GLSync sync, int pname) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer values = stack.mallocInt(1);
            glGetSynciv(sync.getPointer(), pname, null, values);
            return values.get(0);
        }
    }
}
