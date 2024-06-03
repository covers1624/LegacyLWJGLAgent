package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.StubbedMethod;
import net.covers1624.lwjglagent.shim.Shim;
import org.lwjgl.PointerWrapper;
import org.lwjgl.opengl.KHRDebugCallback;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryUtil.memAddress;

@Shim
public class GL43 extends org.lwjgl.opengl.GL43 {

    public static void glDebugMessageCallback(KHRDebugCallback callback) {
        throw new StubbedMethod();
    }

    public static void glObjectPtrLabel(PointerWrapper ptr, ByteBuffer label) {
        glObjectPtrLabel(ptr.getPointer(), label);
    }

    public static void glObjectPtrLabel(PointerWrapper ptr, CharSequence label) {
        glObjectPtrLabel(ptr.getPointer(), label);
    }

    public static void glGetObjectPtrLabel(PointerWrapper ptr, IntBuffer length, ByteBuffer label) {
        glGetObjectPtrLabel(ptr.getPointer(), length, label);
    }

    public static String glGetObjectPtrLabel(PointerWrapper ptr, int bufSize) {
        return glGetObjectPtrLabel(ptr.getPointer(), bufSize);
    }

    public static void glGetFramebufferParameter(int target, int pname, IntBuffer params) {
        glGetFramebufferParameteriv(target, pname, params);
    }

    public static void glGetInternalformat(int target, int internalformat, int pname, LongBuffer params) {
        nglGetInternalformati64v(target, internalformat, pname, params.remaining(), memAddress(params));
    }

    public static void glGetProgramInterface(int program, int programInterface, int pname, IntBuffer params) {
        glGetProgramInterfaceiv(program, programInterface, pname, params);
    }

    public static void glGetProgramResource(int program, int programInterface, int index, IntBuffer props, IntBuffer length, IntBuffer params) {
        glGetProgramResourceiv(program, programInterface, index, props, length, params);
    }
}
