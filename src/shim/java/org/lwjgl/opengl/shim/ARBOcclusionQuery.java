package org.lwjgl.opengl.shim;

import java.nio.IntBuffer;
import net.covers1624.lwjglagent.StubbedMethod;
import net.covers1624.lwjglagent.shim.Shim;

@Shim
public class ARBOcclusionQuery extends org.lwjgl.opengl.ARBOcclusionQuery {

    public static void glGetQueryARB(int target, int pname, IntBuffer params) {
        glGetQueryivARB(target, pname, params);
    }

    public static int glGetQueryARB(int target, int pname) {
        return glGetQueryiARB(target, pname);
    }

    public static void glGetQueryObjectARB(int id, int pname, IntBuffer params) {
        glGetQueryObjectivARB(id, pname, params);
    }

    public static void glGetQueryObjectuARB(int id, int pname, IntBuffer params) {
        glGetQueryObjectuivARB(id, pname, params);
    }
}
