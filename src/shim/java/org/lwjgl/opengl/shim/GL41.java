package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.shim.Shim;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memByteBufferNT1;

@Shim
public class GL41 extends org.lwjgl.opengl.GL41 {

    public static int glCreateShaderProgram(int type, ByteBuffer string) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer strings = stack.pointers(string);
            return glCreateShaderProgramv(type, strings);
        }
    }

    // What the actual fuck is this method, why did this ever exist..
    public static int glCreateShaderProgram(int type, int count, ByteBuffer strings) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer strPtrs = stack.mallocPointer(count);
            long addr = memAddress(strings);
            int strIdx = 0;
            while (strIdx < count) {
                strPtrs.put(strIdx++, addr);
                // jesus christ, please give me a function to just strlen, without buffers, kthx
                addr += memByteBufferNT1(addr).remaining() + 1;
            }
            return glCreateShaderProgramv(type, strPtrs);
        }
    }

    public static int glCreateShaderProgram(int type, ByteBuffer[] strings) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer strPtrs = stack.mallocPointer(strings.length);
            for (int i = 0; i < strings.length; i++) {
                strPtrs.put(i, memAddress(strings[i]));
            }
            return glCreateShaderProgramv(type, strPtrs);
        }
    }

    public static int glCreateShaderProgram(int type, CharSequence string) {
        return glCreateShaderProgramv(type, string);
    }

    public static int glCreateShaderProgram(int type, CharSequence[] strings) {
        return glCreateShaderProgramv(type, strings);
    }

    public static void glGetProgramPipeline(int pipeline, int pname, IntBuffer params) {
        glGetProgramPipelineiv(pipeline, pname, params);
    }

    public static void glProgramUniform1(int program, int location, IntBuffer value) {
        glProgramUniform1iv(program, location, value);
    }

    public static void glProgramUniform2(int program, int location, IntBuffer value) {
        glProgramUniform2iv(program, location, value);
    }

    public static void glProgramUniform3(int program, int location, IntBuffer value) {
        glProgramUniform3iv(program, location, value);
    }

    public static void glProgramUniform4(int program, int location, IntBuffer value) {
        glProgramUniform4iv(program, location, value);
    }

    public static void glProgramUniform1(int program, int location, FloatBuffer value) {
        glProgramUniform1fv(program, location, value);
    }

    public static void glProgramUniform2(int program, int location, FloatBuffer value) {
        glProgramUniform2fv(program, location, value);
    }

    public static void glProgramUniform3(int program, int location, FloatBuffer value) {
        glProgramUniform3fv(program, location, value);
    }

    public static void glProgramUniform4(int program, int location, FloatBuffer value) {
        glProgramUniform4fv(program, location, value);
    }

    public static void glProgramUniform1(int program, int location, DoubleBuffer value) {
        glProgramUniform1dv(program, location, value);
    }

    public static void glProgramUniform2(int program, int location, DoubleBuffer value) {
        glProgramUniform2dv(program, location, value);
    }

    public static void glProgramUniform3(int program, int location, DoubleBuffer value) {
        glProgramUniform3dv(program, location, value);
    }

    public static void glProgramUniform4(int program, int location, DoubleBuffer value) {
        glProgramUniform4dv(program, location, value);
    }

    public static void glProgramUniform1u(int program, int location, IntBuffer value) {
        glProgramUniform1uiv(program, location, value);
    }

    public static void glProgramUniform2u(int program, int location, IntBuffer value) {
        glProgramUniform2uiv(program, location, value);
    }

    public static void glProgramUniform3u(int program, int location, IntBuffer value) {
        glProgramUniform3uiv(program, location, value);
    }

    public static void glProgramUniform4u(int program, int location, IntBuffer value) {
        glProgramUniform4uiv(program, location, value);
    }

    public static void glProgramUniformMatrix2(int program, int location, boolean transpose, FloatBuffer value) {
        glProgramUniformMatrix2fv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix3(int program, int location, boolean transpose, FloatBuffer value) {
        glProgramUniformMatrix3fv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix4(int program, int location, boolean transpose, FloatBuffer value) {
        glProgramUniformMatrix4fv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix2(int program, int location, boolean transpose, DoubleBuffer value) {
        glProgramUniformMatrix2dv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix3(int program, int location, boolean transpose, DoubleBuffer value) {
        glProgramUniformMatrix3dv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix4(int program, int location, boolean transpose, DoubleBuffer value) {
        glProgramUniformMatrix4dv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix2x3(int program, int location, boolean transpose, FloatBuffer value) {
        glProgramUniformMatrix2x3fv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix3x2(int program, int location, boolean transpose, FloatBuffer value) {
        glProgramUniformMatrix3x2fv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix2x4(int program, int location, boolean transpose, FloatBuffer value) {
        glProgramUniformMatrix2x4fv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix4x2(int program, int location, boolean transpose, FloatBuffer value) {
        glProgramUniformMatrix4x2fv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix3x4(int program, int location, boolean transpose, FloatBuffer value) {
        glProgramUniformMatrix3x4fv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix4x3(int program, int location, boolean transpose, FloatBuffer value) {
        glProgramUniformMatrix4x3fv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix2x3(int program, int location, boolean transpose, DoubleBuffer value) {
        glProgramUniformMatrix2x3dv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix3x2(int program, int location, boolean transpose, DoubleBuffer value) {
        glProgramUniformMatrix3x2dv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix2x4(int program, int location, boolean transpose, DoubleBuffer value) {
        glProgramUniformMatrix2x4dv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix4x2(int program, int location, boolean transpose, DoubleBuffer value) {
        glProgramUniformMatrix4x2dv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix3x4(int program, int location, boolean transpose, DoubleBuffer value) {
        glProgramUniformMatrix3x4dv(program, location, transpose, value);
    }

    public static void glProgramUniformMatrix4x3(int program, int location, boolean transpose, DoubleBuffer value) {
        glProgramUniformMatrix4x3dv(program, location, transpose, value);
    }

    public static void glVertexAttribL1(int index, DoubleBuffer v) {
        glVertexAttribL1dv(index, v);
    }

    public static void glVertexAttribL2(int index, DoubleBuffer v) {
        glVertexAttribL2dv(index, v);
    }

    public static void glVertexAttribL3(int index, DoubleBuffer v) {
        glVertexAttribL3dv(index, v);
    }

    public static void glVertexAttribL4(int index, DoubleBuffer v) {
        glVertexAttribL4dv(index, v);
    }

    public static void glVertexAttribLPointer(int index, int size, int stride, long pointer_buffer_offset) {
        nglVertexAttribLPointer(index, size, GL_DOUBLE, stride, pointer_buffer_offset);
    }

    public static void glGetVertexAttribL(int index, int pname, DoubleBuffer params) {
        glGetVertexAttribLdv(index, pname, params);
    }

    public static void glViewportArray(int first, FloatBuffer v) {
        glViewportArrayv(first, v);
    }

    public static void glViewportIndexed(int index, FloatBuffer v) {
        glViewportIndexedfv(index, v);
    }

    public static void glScissorArray(int first, IntBuffer v) {
        glScissorArrayv(first, v);
    }

    public static void glScissorIndexed(int index, IntBuffer v) {
        glScissorIndexedv(index, v);
    }

    public static void glDepthRangeArray(int first, DoubleBuffer v) {
        glDepthRangeArrayv(first, v);
    }

    public static void glGetFloat(int target, int index, FloatBuffer data) {
        glGetFloati_v(target, index, data);
    }

    public static float glGetFloat(int target, int index) {
        return glGetFloati(target, index);
    }

    public static void glGetDouble(int target, int index, DoubleBuffer data) {
        glGetDoublei_v(target, index, data);
    }

    public static double glGetDouble(int target, int index) {
        return glGetDoublei(target, index);
    }
}
