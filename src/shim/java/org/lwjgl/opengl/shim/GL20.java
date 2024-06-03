package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.shim.Shim;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import java.nio.*;

import static org.lwjgl.system.MemoryUtil.memASCII;
import static org.lwjgl.system.MemoryUtil.memAddress;

@Shim
public class GL20 extends org.lwjgl.opengl.GL20 {

    public static void glShaderSource(int shader, ByteBuffer string) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer sPtr = stack.pointers(string);
            IntBuffer lPtr = stack.ints(string.remaining());
            glShaderSource(shader, sPtr, lPtr);
        }
    }

    public static void glUniform1(int location, FloatBuffer values) {
        glUniform1fv(location, values);
    }

    public static void glUniform2(int location, FloatBuffer values) {
        glUniform2fv(location, values);
    }

    public static void glUniform3(int location, FloatBuffer values) {
        glUniform3fv(location, values);
    }

    public static void glUniform4(int location, FloatBuffer values) {
        glUniform4fv(location, values);
    }

    public static void glUniform1(int location, IntBuffer values) {
        glUniform1iv(location, values);
    }

    public static void glUniform2(int location, IntBuffer values) {
        glUniform2iv(location, values);
    }

    public static void glUniform3(int location, IntBuffer values) {
        glUniform3iv(location, values);
    }

    public static void glUniform4(int location, IntBuffer values) {
        glUniform4iv(location, values);
    }

    public static void glUniformMatrix2(int location, boolean transpose, FloatBuffer matrices) {
        glUniformMatrix2fv(location, transpose, matrices);
    }

    public static void glUniformMatrix3(int location, boolean transpose, FloatBuffer matrices) {
        glUniformMatrix3fv(location, transpose, matrices);
    }

    public static void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrices) {
        glUniformMatrix4fv(location, transpose, matrices);
    }

    public static void glGetShader(int shader, int pname, IntBuffer params) {
        glGetShaderiv(shader, pname, params);
    }

    public static int glGetShader(int shader, int pname) {
        return glGetShaderi(shader, pname);
    }

    public static void glGetProgram(int program, int pname, IntBuffer params) {
        glGetProgramiv(program, pname, params);
    }

    public static int glGetProgram(int program, int pname) {
        return glGetProgrami(program, pname);
    }

    public static String glGetActiveUniform(int program, int index, int maxLength, IntBuffer sizeType) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer len = stack.mallocInt(1);
            IntBuffer size = stack.mallocInt(1);
            IntBuffer type = stack.mallocInt(1);
            ByteBuffer name = stack.malloc(maxLength);
            glGetActiveUniform(program, index, len, size, type, name);
            sizeType.put(0, size.get(0));
            sizeType.put(1, type.get(0));
            return memASCII(name, len.get(0));
        }
    }

    public static String glGetActiveUniform(int program, int index, int maxLength) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer len = stack.mallocInt(1);
            IntBuffer size = stack.mallocInt(1);
            IntBuffer type = stack.mallocInt(1);
            ByteBuffer name = stack.malloc(maxLength);
            glGetActiveUniform(program, index, len, size, type, name);
            return memASCII(name, len.get(0));
        }
    }

    public static int glGetActiveUniformSize(int program, int index) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer len = stack.mallocInt(1);
            IntBuffer size = stack.mallocInt(1);
            IntBuffer type = stack.mallocInt(1);
            ByteBuffer name = stack.malloc(1);
            glGetActiveUniform(program, index, len, size, type, name);
            return size.get(0);
        }
    }

    public static int glGetActiveUniformType(int program, int index) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer len = stack.mallocInt(1);
            IntBuffer size = stack.mallocInt(1);
            IntBuffer type = stack.mallocInt(1);
            ByteBuffer name = stack.malloc(1);
            glGetActiveUniform(program, index, len, size, type, name);
            return type.get(0);
        }
    }

    public static void glGetUniform(int program, int location, FloatBuffer params) {
        glGetUniformfv(program, location, params);
    }

    public static void glGetUniform(int program, int location, IntBuffer params) {
        glGetUniformiv(program, location, params);
    }

    public static void glVertexAttribPointer(int index, int size, boolean normalized, int stride, DoubleBuffer buffer) {
        glVertexAttribPointer(index, size, GL11.GL_DOUBLE, normalized, stride, memAddress(buffer));
    }

    public static void glVertexAttribPointer(int index, int size, boolean normalized, int stride, FloatBuffer buffer) {
        glVertexAttribPointer(index, size, GL11.GL_FLOAT, normalized, stride, buffer);
    }

    public static void glVertexAttribPointer(int index, int size, boolean unsigned, boolean normalized, int stride, ByteBuffer buffer) {
        glVertexAttribPointer(index, size, unsigned ? GL11.GL_UNSIGNED_BYTE : GL11.GL_BYTE, normalized, stride, buffer);
    }

    public static void glVertexAttribPointer(int index, int size, boolean unsigned, boolean normalized, int stride, IntBuffer buffer) {
        glVertexAttribPointer(index, size, unsigned ? GL11.GL_UNSIGNED_INT : GL11.GL_INT, normalized, stride, buffer);
    }

    public static void glVertexAttribPointer(int index, int size, boolean unsigned, boolean normalized, int stride, ShortBuffer buffer) {
        glVertexAttribPointer(index, size, unsigned ? GL11.GL_UNSIGNED_SHORT : GL11.GL_SHORT, normalized, stride, buffer);
    }

    public static void glGetVertexAttrib(int index, int pname, FloatBuffer params) {
        glGetVertexAttribfv(index, pname, params);
    }

    public static void glGetVertexAttrib(int index, int pname, DoubleBuffer params) {
        glGetVertexAttribdv(index, pname, params);
    }

    public static void glGetVertexAttrib(int index, int pname, IntBuffer params) {
        glGetVertexAttribiv(index, pname, params);
    }

    public static ByteBuffer glGetVertexAttribPointer(int index, int pname, long result_size) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer pointer = stack.mallocPointer(1);
            glGetVertexAttribPointerv(index, pname, pointer);
            return pointer.getByteBuffer((int) result_size);
        }
    }

    public static void glGetVertexAttribPointer(int index, int pname, ByteBuffer pointer) {
        nglGetVertexAttribPointerv(index, pname, memAddress(pointer));
    }

    public static String glGetActiveAttrib(int program, int index, int maxLength, IntBuffer sizeType) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer len = stack.mallocInt(1);
            IntBuffer size = stack.mallocInt(1);
            IntBuffer type = stack.mallocInt(1);
            ByteBuffer name = stack.malloc(maxLength);
            glGetActiveAttrib(program, index, len, size, type, name);
            sizeType.put(0, size.get(0));
            sizeType.put(1, type.get(0));
            return memASCII(name, len.get(0));
        }
    }

    public static String glGetActiveAttrib(int program, int index, int maxLength) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer len = stack.mallocInt(1);
            IntBuffer size = stack.mallocInt(1);
            IntBuffer type = stack.mallocInt(1);
            ByteBuffer name = stack.malloc(maxLength);
            glGetActiveUniform(program, index, len, size, type, name);
            return memASCII(name, len.get(0));
        }
    }

    public static int glGetActiveAttribSize(int program, int index) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer len = stack.mallocInt(1);
            IntBuffer size = stack.mallocInt(1);
            IntBuffer type = stack.mallocInt(1);
            ByteBuffer name = stack.malloc(1);
            glGetActiveUniform(program, index, len, size, type, name);
            return size.get(0);
        }
    }

    public static int glGetActiveAttribType(int program, int index) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer len = stack.mallocInt(1);
            IntBuffer size = stack.mallocInt(1);
            IntBuffer type = stack.mallocInt(1);
            ByteBuffer name = stack.malloc(1);
            glGetActiveUniform(program, index, len, size, type, name);
            return type.get(0);
        }
    }
}
