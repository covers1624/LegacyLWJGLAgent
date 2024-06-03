package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.shim.Shim;
import org.lwjgl.system.MemoryUtil;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

@Shim
public class GL13 extends org.lwjgl.opengl.GL13 {

    public static void glCompressedTexImage1D(int target, int level, int internalformat, int width, int border, int imageSize) {
        glCompressedTexImage1D(target, level, internalformat, width, border, imageSize, MemoryUtil.NULL);
    }

    public static void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize) {
        glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, MemoryUtil.NULL);
    }

    public static void glCompressedTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int imageSize) {
        glCompressedTexImage3D(target, level, internalformat, width, height, depth, border, imageSize, MemoryUtil.NULL);
    }

    public static void glGetCompressedTexImage(int target, int lod, IntBuffer img) {
        glGetCompressedTexImage(target, lod, MemoryUtil.memAddress(img));
    }

    public static void glGetCompressedTexImage(int target, int lod, ShortBuffer img) {
        glGetCompressedTexImage(target, lod, MemoryUtil.memAddress(img));
    }

    public static void glLoadTransposeMatrix(FloatBuffer m) {
        glLoadTransposeMatrixf(m);
    }

    public static void glLoadTransposeMatrix(DoubleBuffer m) {
        glLoadTransposeMatrixd(m);
    }

    public static void glMultTransposeMatrix(FloatBuffer m) {
        glMultTransposeMatrixf(m);
    }

    public static void glMultTransposeMatrix(DoubleBuffer m) {
        glMultTransposeMatrixd(m);
    }
}
