package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.shim.Shim;
import org.lwjgl.PointerBuffer;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryUtil.memAddress;

@Shim
public class GL44 extends org.lwjgl.opengl.GL44 {

    public static void glBufferStorage(int target, LongBuffer data, int flags) {
        nglBufferStorage(target, data.remaining(), memAddress(data), flags);
    }

    public static void glClearTexImage(int texture, int level, int format, int type, LongBuffer data) {
        nglClearTexImage(texture, level, format, type, memAddress(data));
    }

    public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, LongBuffer data) {
        nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, memAddress(data));
    }

    public static void glBindBuffersBase(int target, int first, int count, IntBuffer buffers) {
        nglBindBuffersBase(target, first, count, memAddress(buffers));
    }

    public static void glBindBuffersRange(int target, int first, int count, IntBuffer buffers, PointerBuffer offsets, PointerBuffer sizes) {
        nglBindBuffersRange(target, first, count, memAddress(buffers), memAddress(offsets), memAddress(sizes));
    }

    public static void glBindTextures(int first, int count, IntBuffer textures) {
        nglBindTextures(first, count, memAddress(textures));
    }

    public static void glBindSamplers(int first, int count, IntBuffer samplers) {
        nglBindSamplers(first, count, memAddress(samplers));
    }

    public static void glBindImageTextures(int first, int count, IntBuffer textures) {
        nglBindImageTextures(first, count, memAddress(textures));
    }

    public static void glBindVertexBuffers(int first, int count, IntBuffer buffers, PointerBuffer offsets, IntBuffer strides) {
        nglBindVertexBuffers(first, count, memAddress(buffers), memAddress(offsets), memAddress(strides));
    }
}
