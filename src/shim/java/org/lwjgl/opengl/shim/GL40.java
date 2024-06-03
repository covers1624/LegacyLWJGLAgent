package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.shim.Shim;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@Shim
public class GL40 extends org.lwjgl.opengl.GL40 {

    public static void glUniform1(int location, DoubleBuffer value) {
        glUniform1dv(location, value);
    }

    public static void glUniform2(int location, DoubleBuffer value) {
        glUniform2dv(location, value);
    }

    public static void glUniform3(int location, DoubleBuffer value) {
        glUniform3dv(location, value);
    }

    public static void glUniform4(int location, DoubleBuffer value) {
        glUniform4dv(location, value);
    }

    public static void glUniformMatrix2(int location, boolean transpose, DoubleBuffer value) {
        glUniformMatrix2dv(location, transpose, value);
    }

    public static void glUniformMatrix3(int location, boolean transpose, DoubleBuffer value) {
        glUniformMatrix3dv(location, transpose, value);
    }

    public static void glUniformMatrix4(int location, boolean transpose, DoubleBuffer value) {
        glUniformMatrix4dv(location, transpose, value);
    }

    public static void glUniformMatrix2x3(int location, boolean transpose, DoubleBuffer value) {
        glUniformMatrix2x3dv(location, transpose, value);
    }

    public static void glUniformMatrix2x4(int location, boolean transpose, DoubleBuffer value) {
        glUniformMatrix2x4dv(location, transpose, value);
    }

    public static void glUniformMatrix3x2(int location, boolean transpose, DoubleBuffer value) {
        glUniformMatrix3x2dv(location, transpose, value);
    }

    public static void glUniformMatrix3x4(int location, boolean transpose, DoubleBuffer value) {
        glUniformMatrix3x4dv(location, transpose, value);
    }

    public static void glUniformMatrix4x2(int location, boolean transpose, DoubleBuffer value) {
        glUniformMatrix4x2dv(location, transpose, value);
    }

    public static void glUniformMatrix4x3(int location, boolean transpose, DoubleBuffer value) {
        glUniformMatrix4x3dv(location, transpose, value);
    }

    public static void glGetUniform(int program, int location, DoubleBuffer params) {
        glGetUniformdv(program, location, params);
    }

    public static void glGetActiveSubroutineUniform(int program, int shadertype, int index, int pname, IntBuffer values) {
        glGetActiveSubroutineUniformiv(program, shadertype, index, pname, values);
    }

    public static int glGetActiveSubroutineUniform(int program, int shadertype, int index, int pname) {
        return glGetActiveSubroutineUniformi(program, shadertype, index, pname);
    }

    public static void glUniformSubroutinesu(int shadertype, IntBuffer indices) {
        glUniformSubroutinesuiv(shadertype, indices);
    }

    public static void glGetUniformSubroutineu(int shadertype, int location, IntBuffer params) {
        glGetUniformSubroutineuiv(shadertype, location, params);
    }

    public static int glGetUniformSubroutineu(int shadertype, int location) {
        return glGetUniformSubroutineui(shadertype, location);
    }

    public static void glGetProgramStage(int program, int shadertype, int pname, IntBuffer values) {
        glGetProgramStageiv(program, shadertype, pname, values);
    }

    public static int glGetProgramStage(int program, int shadertype, int pname) {
        return glGetProgramStagei(program, shadertype, pname);
    }

    public static void glPatchParameter(int pname, FloatBuffer values) {
        glPatchParameterfv(pname, values);
    }

    public static void glGetQueryIndexed(int target, int index, int pname, IntBuffer params) {
        glGetQueryIndexediv(target, index, pname, params);
    }

    public static int glGetQueryIndexed(int target, int index, int pname) {
        return glGetQueryIndexedi(target, index, pname);
    }
}
