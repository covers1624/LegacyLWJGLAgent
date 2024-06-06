package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.shim.Shim;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import java.nio.*;

import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memByteBuffer;

@Shim
public class GL11 extends org.lwjgl.opengl.GL11 {

    public static void glColorPointer(int size, int stride, DoubleBuffer pointer) {
        glColorPointer(size, GL_DOUBLE, stride, memAddress(pointer));
    }

    public static void glColorPointer(int size, int stride, FloatBuffer pointer) {
        glColorPointer(size, GL_FLOAT, stride, memAddress(pointer));
    }

    public static void glColorPointer(int size, boolean unsigned, int stride, ByteBuffer pointer) {
        glColorPointer(size, unsigned ? GL_UNSIGNED_BYTE : GL_BYTE, stride, memAddress(pointer));
    }

    public static void glDrawElements(int mode, int count, int type, ByteBuffer indices) {
        glDrawElements(mode, count, type, memAddress(indices));
    }

    public static void glGetPixelMap(int map, FloatBuffer values) {
        glGetPixelMapfv(map, values);
    }

    public static void glGetPixelMapu(int map, IntBuffer values) {
        glGetPixelMapuiv(map, values);
    }

    public static void glGetPixelMapu(int map, ShortBuffer values) {
        glGetPixelMapusv(map, values);
    }

    public static void glGetMaterial(int face, int pname, FloatBuffer params) {
        glGetMaterialfv(face, pname, params);
    }

    public static void glGetMaterial(int face, int pname, IntBuffer params) {
        glGetMaterialiv(face, pname, params);
    }

    public static void glGetMap(int target, int query, FloatBuffer v) {
        glGetMapfv(target, query, v);
    }

    public static void glGetMap(int target, int query, DoubleBuffer v) {
        glGetMapdv(target, query, v);
    }

    public static void glGetMap(int target, int query, IntBuffer v) {
        glGetMapiv(target, query, v);
    }

    public static void glGetLight(int light, int pname, FloatBuffer params) {
        glGetLightfv(light, pname, params);
    }

    public static void glGetLight(int light, int pname, IntBuffer params) {
        glGetLightiv(light, pname, params);
    }

    public static void glGetBoolean(int pname, ByteBuffer params) {
        glGetBooleanv(pname, params);
    }

    public static void glGetDouble(int pname, DoubleBuffer params) {
        glGetDoublev(pname, params);
    }

    public static void glGetFloat(int pname, FloatBuffer params) {
        glGetFloatv(pname, params);
    }

    public static void glGetInteger(int pname, IntBuffer params) {
        glGetIntegerv(pname, params);
    }

    public static void glFog(int pname, FloatBuffer params) {
        glFogfv(pname, params);
    }

    public static void glFog(int pname, IntBuffer params) {
        glFogiv(pname, params);
    }

    public static ByteBuffer glGetPointer(int pname, long result_size) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer p = stack.mallocPointer(1);
            glGetPointerv(pname, p);
            return memByteBuffer(p.get(), (int) result_size);
        }
    }

    public static void glGetTexParameter(int target, int pname, FloatBuffer params) {
        glGetTexParameterfv(target, pname, params);
    }

    public static void glGetTexParameter(int target, int pname, IntBuffer params) {
        glGetTexParameteriv(target, pname, params);
    }

    public static void glGetTexLevelParameter(int target, int level, int pname, FloatBuffer params) {
        glGetTexLevelParameterfv(target, level, pname, params);
    }

    public static void glGetTexLevelParameter(int target, int level, int pname, IntBuffer params) {
        glGetTexLevelParameteriv(target, level, pname, params);
    }

    public static void glGetTexGen(int coord, int pname, IntBuffer params) {
        glTexGeniv(coord, pname, params);
    }

    public static void glGetTexGen(int coord, int pname, FloatBuffer params) {
        glTexGenfv(coord, pname, params);
    }

    public static void glGetTexGen(int coord, int pname, DoubleBuffer params) {
        glTexGendv(coord, pname, params);
    }

    public static void glGetTexEnv(int coord, int pname, IntBuffer params) {
        glGetTexEnviv(coord, pname, params);
    }

    public static void glGetTexEnv(int coord, int pname, FloatBuffer params) {
        glGetTexEnvfv(coord, pname, params);
    }

    public static void glMaterial(int face, int pname, FloatBuffer params) {
        glMaterialfv(face, pname, params);
    }

    public static void glMaterial(int face, int pname, IntBuffer params) {
        glMaterialiv(face, pname, params);
    }

    public static void glLoadMatrix(FloatBuffer m) {
        glLoadMatrixf(m);
    }

    public static void glLoadMatrix(DoubleBuffer m) {
        glLoadMatrixd(m);
    }

    public static void glLightModel(int pname, FloatBuffer params) {
        glLightModelfv(pname, params);
    }

    public static void glLightModel(int pname, IntBuffer params) {
        glLightModeliv(pname, params);
    }

    public static void glLight(int light, int pname, FloatBuffer params) {
        glLightfv(light, pname, params);
    }

    public static void glLight(int light, int pname, IntBuffer params) {
        glLightiv(light, pname, params);
    }

    public static void glPixelMap(int map, FloatBuffer values) {
        glPixelMapfv(map, values);
    }

    public static void glPixelMapu(int map, IntBuffer values) {
        glPixelMapuiv(map, values);
    }

    public static void glPixelMapu(int map, ShortBuffer values) {
        glPixelMapusv(map, values);
    }

    public static void glNormalPointer(int stride, ByteBuffer pointer) {
        glNormalPointer(GL_BYTE, stride, pointer);
    }

    public static void glNormalPointer(int stride, DoubleBuffer pointer) {
        glNormalPointer(GL_DOUBLE, stride, memAddress(pointer));
    }

    public static void glNormalPointer(int stride, FloatBuffer pointer) {
        glNormalPointer(GL_FLOAT, stride, pointer);
    }

    public static void glNormalPointer(int stride, IntBuffer pointer) {
        glNormalPointer(GL_INT, stride, pointer);
    }

    public static void glMultMatrix(FloatBuffer m) {
        glMultMatrixf(m);
    }

    public static void glMultMatrix(DoubleBuffer m) {
        glMultMatrixd(m);
    }

    public static void glReadPixels(int x, int y, int width, int height, int format, int type, DoubleBuffer pixels) {
        glReadPixels(x, y, width, height, format, type, memAddress(pixels));
    }

    public static void glVertexPointer(int size, int stride, DoubleBuffer pointer) {
        glVertexPointer(size, GL_DOUBLE, stride, memAddress(pointer));
    }

    public static void glVertexPointer(int size, int stride, FloatBuffer pointer) {
        glVertexPointer(size, GL_FLOAT, stride, pointer);
    }

    public static void glVertexPointer(int size, int stride, IntBuffer pointer) {
        glVertexPointer(size, GL_INT, stride, pointer);
    }

    public static void glVertexPointer(int size, int stride, ShortBuffer pointer) {
        glVertexPointer(size, GL_SHORT, stride, pointer);
    }

    public static void glTexParameter(int target, int pname, FloatBuffer param) {
        glTexParameterfv(target, pname, param);
    }

    public static void glTexParameter(int target, int pname, IntBuffer param) {
        glTexParameteriv(target, pname, param);
    }

    public static void glTexGen(int coord, int pname, FloatBuffer params) {
        glTexGenfv(coord, pname, params);
    }

    public static void glTexGen(int coord, int pname, DoubleBuffer params) {
        glTexGendv(coord, pname, params);
    }

    public static void glTexGen(int coord, int pname, IntBuffer params) {
        glTexGeniv(coord, pname, params);
    }

    public static void glTexEnv(int target, int pname, FloatBuffer params) {
        glTexEnvfv(target, pname, params);
    }

    public static void glTexEnv(int target, int pname, IntBuffer params) {
        glTexEnviv(target, pname, params);
    }

    public static void glTexCoordPointer(int size, int stride, DoubleBuffer pointer) {
        glTexCoordPointer(size, GL_DOUBLE, stride, memAddress(pointer));
    }

    public static void glTexCoordPointer(int size, int stride, FloatBuffer pointer) {
        glTexCoordPointer(size, GL_FLOAT, stride, pointer);
    }

    public static void glTexCoordPointer(int size, int stride, IntBuffer pointer) {
        glTexCoordPointer(size, GL_INT, stride, pointer);
    }

    public static void glTexCoordPointer(int size, int stride, ShortBuffer pointer) {
        glTexCoordPointer(size, GL_SHORT, stride, pointer);
    }
}
