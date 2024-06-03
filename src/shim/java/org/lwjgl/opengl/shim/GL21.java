package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.shim.Shim;

import java.nio.FloatBuffer;

@Shim
public class GL21 extends org.lwjgl.opengl.GL21 {

    public static void glUniformMatrix2x3(int location, boolean transpose, FloatBuffer matrices) {
        glUniformMatrix2x3fv(location, transpose, matrices);
    }

    public static void glUniformMatrix3x2(int location, boolean transpose, FloatBuffer matrices) {
        glUniformMatrix3x2fv(location, transpose, matrices);
    }

    public static void glUniformMatrix2x4(int location, boolean transpose, FloatBuffer matrices) {
        glUniformMatrix2x4fv(location, transpose, matrices);
    }

    public static void glUniformMatrix4x2(int location, boolean transpose, FloatBuffer matrices) {
        glUniformMatrix4x2fv(location, transpose, matrices);
    }

    public static void glUniformMatrix3x4(int location, boolean transpose, FloatBuffer matrices) {
        glUniformMatrix3x4fv(location, transpose, matrices);
    }

    public static void glUniformMatrix4x3(int location, boolean transpose, FloatBuffer matrices) {
        glUniformMatrix4x3fv(location, transpose, matrices);
    }
}
