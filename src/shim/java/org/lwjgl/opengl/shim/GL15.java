package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.shim.Named;
import net.covers1624.lwjglagent.shim.Shim;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

@Shim
public class GL15 extends org.lwjgl.opengl.GL15 {

    public static void glGetBufferParameter(int target, int pname, IntBuffer params) {
        glGetBufferParameteriv(target, pname, params);
    }

    public static int glGetBufferParameter(int target, int pname) {
        return glGetBufferParameteri(target, pname);
    }

    @Named ("glGetBufferPointer")
    public static ByteBuffer glGetBufferPointer2(int target, int pname) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer buffer = stack.mallocPointer(1);
            glGetBufferPointerv(target, pname, buffer);
            return buffer.getByteBuffer(glGetBufferParameteri(target, GL_BUFFER_SIZE));
        }
    }

    public static void glGetQuery(int target, int pname, IntBuffer params) {
        glGetQueryiv(target, pname, params);
    }

    public static int glGetQuery(int target, int pname) {
        return glGetQueryi(target, pname);
    }

    public static void glGetQueryObject(int id, int pname, IntBuffer params) {
        glGetQueryObjectiv(id, pname, params);
    }

    public static void glGetQueryObjectu(int id, int pname, IntBuffer params) {
        glGetQueryObjectuiv(id, pname, params);
    }
}
