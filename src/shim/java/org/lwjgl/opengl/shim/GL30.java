package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.shim.Shim;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.system.MemoryUtil.memAddress;

@Shim
public class GL30 extends org.lwjgl.opengl.GL30 {

    public static void glClearBuffer(int buffer, int drawbuffer, FloatBuffer value) {
        glClearBufferfv(buffer, drawbuffer, value);
    }

    public static void glClearBuffer(int buffer, int drawbuffer, IntBuffer value) {
        glClearBufferiv(buffer, drawbuffer, value);
    }

    public static void glClearBufferu(int buffer, int drawbuffer, IntBuffer value) {
        glClearBufferuiv(buffer, drawbuffer, value);
    }

    public static void glVertexAttribI1(int index, IntBuffer v) {
        glVertexAttribI1iv(index, v);
    }

    public static void glVertexAttribI2(int index, IntBuffer v) {
        glVertexAttribI2iv(index, v);
    }

    public static void glVertexAttribI3(int index, IntBuffer v) {
        glVertexAttribI3iv(index, v);
    }

    public static void glVertexAttribI4(int index, IntBuffer v) {
        glVertexAttribI4iv(index, v);
    }

    public static void glVertexAttribI1u(int index, IntBuffer v) {
        glVertexAttribI1uiv(index, v);
    }

    public static void glVertexAttribI2u(int index, IntBuffer v) {
        glVertexAttribI2uiv(index, v);
    }

    public static void glVertexAttribI3u(int index, IntBuffer v) {
        glVertexAttribI3uiv(index, v);
    }

    public static void glVertexAttribI4u(int index, IntBuffer v) {
        glVertexAttribI4uiv(index, v);
    }

    public static void glVertexAttribI4(int index, ByteBuffer v) {
        nglVertexAttribI4iv(index, memAddress(v));
    }

    public static void glVertexAttribI4(int index, ShortBuffer v) {
        nglVertexAttribI4sv(index, memAddress(v));
    }

    public static void glVertexAttribI4u(int index, ByteBuffer v) {
        nglVertexAttribI4uiv(index, memAddress(v));
    }

    public static void glVertexAttribI4u(int index, ShortBuffer v) {
        nglVertexAttribI4usv(index, memAddress(v));
    }

    public static void glGetVertexAttribI(int index, int pname, IntBuffer params) {
        glGetVertexAttribIiv(index, pname, params);
    }

    public static void glGetVertexAttribIu(int index, int pname, IntBuffer params) {
        glGetVertexAttribIuiv(index, pname, params);
    }

    public static void glUniform1u(int location, IntBuffer value) {
        glUniform1uiv(location, value);
    }

    public static void glUniform2u(int location, IntBuffer value) {
        glUniform2uiv(location, value);
    }

    public static void glUniform3u(int location, IntBuffer value) {
        glUniform3uiv(location, value);
    }

    public static void glUniform4u(int location, IntBuffer value) {
        glUniform4uiv(location, value);
    }

    public static void glGetUniformu(int program, int location, IntBuffer params) {
        glGetUniformuiv(program, location, params);
    }

    public static void glGetRenderbufferParameter(int target, int pname, IntBuffer params) {
        glGetRenderbufferParameteriv(target, pname, params);
    }

    public static int glGetRenderbufferParameter(int target, int pname) {
        return glGetRenderbufferParameteri(target, pname);
    }

    public static void glGetFramebufferAttachmentParameter(int target, int attachment, int pname, IntBuffer params) {
        glGetFramebufferAttachmentParameteriv(target, attachment, pname, params);
    }

    public static int glGetFramebufferAttachmentParameter(int target, int attachment, int pname) {
        return glGetFramebufferAttachmentParameteri(target, attachment, pname);
    }

    public static void glTexParameterI(int target, int pname, IntBuffer params) {
        glTexParameterIiv(target, pname, params);
    }

    public static void glTexParameterIu(int target, int pname, IntBuffer params) {
        glTexParameterIuiv(target, pname, params);
    }

    public static void glGetTexParameterI(int target, int pname, IntBuffer params) {
        glGetTexParameterIiv(target, pname, params);
    }

    public static void glGetTexParameterIu(int target, int pname, IntBuffer params) {
        glGetTexParameterIuiv(target, pname, params);
    }

    public static void glGetBoolean(int value, int index, ByteBuffer data) {
        glGetBooleani_v(value, index, data);
    }

    public static boolean glGetBoolean(int value, int index) {
        return glGetBooleani(value, index);
    }

    public static void glGetInteger(int value, int index, IntBuffer data) {
        glGetIntegeri_v(value, index, data);
    }

    public static int glGetInteger(int value, int index) {
        return glGetIntegeri(value, index);
    }

    public static void glTransformFeedbackVaryings(int program, int count, ByteBuffer varyings, int bufferMode) {
        nglTransformFeedbackVaryings(program, count, memAddress(varyings), bufferMode);
    }
}
