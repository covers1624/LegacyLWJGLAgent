package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.shim.Shim;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryUtil.memAddress;

@Shim
public class GL31 extends org.lwjgl.opengl.GL31 {

    public static void glGetUniformIndices(int program, ByteBuffer uniformNames, IntBuffer uniformIndices) {
        nglGetUniformIndices(program, uniformNames.remaining(), memAddress(uniformNames), memAddress(uniformIndices));
    }

    public static void glGetActiveUniforms(int program, IntBuffer uniformIndices, int pname, IntBuffer params) {
        glGetActiveUniformsiv(program, uniformIndices, pname, params);
    }

    public static int glGetActiveUniforms(int program, int uniformIndex, int pname) {
        return glGetActiveUniformsi(program, uniformIndex, pname);
    }

    public static void glGetActiveUniformBlock(int program, int uniformBlockIndex, int pname, IntBuffer params) {
        glGetActiveUniformBlockiv(program, uniformBlockIndex, pname, params);
    }

    public static int glGetActiveUniformBlock(int program, int uniformBlockIndex, int pname) {
        return glGetActiveUniformBlocki(program, uniformBlockIndex, pname);
    }
}
