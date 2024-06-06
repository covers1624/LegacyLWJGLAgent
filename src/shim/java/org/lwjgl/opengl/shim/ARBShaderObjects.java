package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.shim.Shim;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryUtil.memASCII;

@Shim
public class ARBShaderObjects extends org.lwjgl.opengl.ARBShaderObjects {

    public static void glShaderSourceARB(int shader, ByteBuffer string) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer sPtr = stack.pointers(string);
            IntBuffer lPtr = stack.ints(string.remaining());
            glShaderSourceARB(shader, sPtr, lPtr);
        }
    }

    public static void glUniform1ARB(int location, FloatBuffer values) {
        glUniform1fvARB(location, values);
    }

    public static void glUniform2ARB(int location, FloatBuffer values) {
        glUniform2fvARB(location, values);
    }

    public static void glUniform3ARB(int location, FloatBuffer values) {
        glUniform3fvARB(location, values);
    }

    public static void glUniform4ARB(int location, FloatBuffer values) {
        glUniform4fvARB(location, values);
    }

    public static void glUniform1ARB(int location, IntBuffer values) {
        glUniform1ivARB(location, values);
    }

    public static void glUniform2ARB(int location, IntBuffer values) {
        glUniform2ivARB(location, values);
    }

    public static void glUniform3ARB(int location, IntBuffer values) {
        glUniform3ivARB(location, values);
    }

    public static void glUniform4ARB(int location, IntBuffer values) {
        glUniform4ivARB(location, values);
    }

    public static void glUniformMatrix2ARB(int location, boolean transpose, FloatBuffer matrices) {
        glUniformMatrix2fvARB(location, transpose, matrices);
    }

    public static void glUniformMatrix3ARB(int location, boolean transpose, FloatBuffer matrices) {
        glUniformMatrix3fvARB(location, transpose, matrices);
    }

    public static void glUniformMatrix4ARB(int location, boolean transpose, FloatBuffer matrices) {
        glUniformMatrix4fvARB(location, transpose, matrices);
    }

    public static void glGetObjectParameterARB(int obj, int pname, FloatBuffer params) {
        glGetObjectParameterfvARB(obj, pname, params);
    }

    public static float glGetObjectParameterfARB(int obj, int pname) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(1);
            glGetObjectParameterfvARB(obj, pname, buffer);
            return buffer.get();
        }
    }

    public static void glGetObjectParameterARB(int obj, int pname, IntBuffer params) {
        glGetObjectParameterivARB(obj, pname, params);
    }

    public static String glGetActiveUniformARB(int programObj, int index, int maxLength, IntBuffer sizeType) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer len = stack.mallocInt(1);
            IntBuffer size = stack.mallocInt(1);
            IntBuffer type = stack.mallocInt(1);
            ByteBuffer name = stack.malloc(maxLength);
            glGetActiveUniformARB(programObj, index, len, size, type, name);
            sizeType.put(0, size.get(0));
            sizeType.put(1, type.get(0));
            return memASCII(name, len.get(0));
        }
    }

    public static String glGetActiveUniformARB(int programObj, int index, int maxLength) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer len = stack.mallocInt(1);
            IntBuffer size = stack.mallocInt(1);
            IntBuffer type = stack.mallocInt(1);
            ByteBuffer name = stack.malloc(maxLength);
            glGetActiveUniformARB(programObj, index, len, size, type, name);
            return memASCII(name, len.get(0));
        }
    }

    public static int glGetActiveUniformSizeARB(int programObj, int index) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer len = stack.mallocInt(1);
            IntBuffer size = stack.mallocInt(1);
            IntBuffer type = stack.mallocInt(1);
            ByteBuffer name = stack.malloc(1);
            glGetActiveUniformARB(programObj, index, len, size, type, name);
            return size.get(0);
        }
    }

    public static int glGetActiveUniformTypeARB(int programObj, int index) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer len = stack.mallocInt(1);
            IntBuffer size = stack.mallocInt(1);
            IntBuffer type = stack.mallocInt(1);
            ByteBuffer name = stack.malloc(1);
            glGetActiveUniformARB(programObj, index, len, size, type, name);
            return type.get(0);
        }
    }

    public static void glGetUniformARB(int programObj, int location, FloatBuffer params) {
        glGetUniformfvARB(programObj, location, params);
    }

    public static void glGetUniformARB(int programObj, int location, IntBuffer params) {
        glGetUniformivARB(programObj, location, params);
    }
}
