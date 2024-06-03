package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.shim.Shim;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

@Shim
public class GL33 extends org.lwjgl.opengl.GL33 {

    public static void glSamplerParameter(int sampler, int pname, IntBuffer params) {
        glSamplerParameteriv(sampler, pname, params);
    }

    public static void glSamplerParameter(int sampler, int pname, FloatBuffer params) {
        glSamplerParameterfv(sampler, pname, params);
    }

    public static void glSamplerParameterI(int sampler, int pname, IntBuffer params) {
        glSamplerParameterIiv(sampler, pname, params);
    }

    public static void glSamplerParameterIu(int sampler, int pname, IntBuffer params) {
        glSamplerParameterIuiv(sampler, pname, params);
    }

    public static void glGetSamplerParameter(int sampler, int pname, IntBuffer params) {
        glGetSamplerParameteriv(sampler, pname, params);
    }

    public static void glGetSamplerParameter(int sampler, int pname, FloatBuffer params) {
        glGetSamplerParameterfv(sampler, pname, params);
    }

    public static void glGetSamplerParameterI(int sampler, int pname, IntBuffer params) {
        glGetSamplerParameterIiv(sampler, pname, params);
    }

    public static void glGetSamplerParameterIu(int sampler, int pname, IntBuffer params) {
        glGetSamplerParameterIuiv(sampler, pname, params);
    }

    public static void glGetQueryObject(int id, int pname, LongBuffer params) {
        glGetQueryObjecti64v(id, pname, params);
    }

    public static long glGetQueryObject(int id, int pname) {
        return glGetQueryObjecti64(id, pname);
    }

    public static void glGetQueryObjectu(int id, int pname, LongBuffer params) {
        glGetQueryObjectui64v(id, pname, params);
    }

    public static long glGetQueryObjectu(int id, int pname) {
        return glGetQueryObjectui64(id, pname);
    }

    public static void glVertexP2u(int type, IntBuffer value) {
        glVertexP2uiv(type, value);
    }

    public static void glVertexP3u(int type, IntBuffer value) {
        glVertexP3uiv(type, value);
    }

    public static void glVertexP4u(int type, IntBuffer value) {
        glVertexP4uiv(type, value);
    }

    public static void glTexCoordP1u(int type, IntBuffer coords) {
        glTexCoordP1uiv(type, coords);
    }

    public static void glTexCoordP2u(int type, IntBuffer coords) {
        glTexCoordP2uiv(type, coords);
    }

    public static void glTexCoordP3u(int type, IntBuffer coords) {
        glTexCoordP3uiv(type, coords);
    }

    public static void glTexCoordP4u(int type, IntBuffer coords) {
        glTexCoordP4uiv(type, coords);
    }

    public static void glMultiTexCoordP1u(int texture, int type, IntBuffer coords) {
        glMultiTexCoordP1uiv(texture, type, coords);
    }

    public static void glMultiTexCoordP2u(int texture, int type, IntBuffer coords) {
        glMultiTexCoordP2uiv(texture, type, coords);
    }

    public static void glMultiTexCoordP3u(int texture, int type, IntBuffer coords) {
        glMultiTexCoordP3uiv(texture, type, coords);
    }

    public static void glMultiTexCoordP4u(int texture, int type, IntBuffer coords) {
        glMultiTexCoordP4uiv(texture, type, coords);
    }

    public static void glNormalP3u(int type, IntBuffer coords) {
        glNormalP3uiv(type, coords);
    }

    public static void glColorP3u(int type, IntBuffer color) {
        glColorP3uiv(type, color);
    }

    public static void glColorP4u(int type, IntBuffer color) {
        glColorP4uiv(type, color);
    }

    public static void glSecondaryColorP3u(int type, IntBuffer color) {
        glSecondaryColorP3uiv(type, color);
    }

    public static void glVertexAttribP1u(int index, int type, boolean normalized, IntBuffer value) {
        glVertexAttribP1uiv(index, type, normalized, value);
    }

    public static void glVertexAttribP2u(int index, int type, boolean normalized, IntBuffer value) {
        glVertexAttribP2uiv(index, type, normalized, value);
    }

    public static void glVertexAttribP3u(int index, int type, boolean normalized, IntBuffer value) {
        glVertexAttribP3uiv(index, type, normalized, value);
    }

    public static void glVertexAttribP4u(int index, int type, boolean normalized, IntBuffer value) {
        glVertexAttribP4uiv(index, type, normalized, value);
    }
}
