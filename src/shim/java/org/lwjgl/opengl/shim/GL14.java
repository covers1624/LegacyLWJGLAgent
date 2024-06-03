package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.shim.Shim;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryUtil.memAddress;

@Shim
public class GL14 extends org.lwjgl.opengl.GL14 {

    public static void glFogCoordPointer(int stride, DoubleBuffer data) {
        glFogCoordPointer(GL11.GL_DOUBLE, stride, memAddress(data));
    }

    public static void glFogCoordPointer(int stride, FloatBuffer data) {
        glFogCoordPointer(GL11.GL_FLOAT, stride, data);
    }

    public static void glPointParameter(int pname, IntBuffer params) {
        glPointParameteriv(pname, params);
    }

    public static void glPointParameter(int pname, FloatBuffer params) {
        glPointParameterfv(pname, params);
    }

    public static void glSecondaryColorPointer(int size, int stride, DoubleBuffer data) {
        glSecondaryColorPointer(size, GL11.GL_DOUBLE, stride, memAddress(data));
    }

    public static void glSecondaryColorPointer(int size, int stride, FloatBuffer data) {
        glSecondaryColorPointer(size, GL11.GL_FLOAT, stride, data);
    }

    public static void glSecondaryColorPointer(int size, boolean unsigned, int stride, ByteBuffer data) {
        glSecondaryColorPointer(size, unsigned ? GL11.GL_UNSIGNED_BYTE : GL11.GL_BYTE, stride, data);
    }
}
