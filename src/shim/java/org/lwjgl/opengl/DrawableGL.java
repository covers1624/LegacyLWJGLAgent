package org.lwjgl.opengl;

import net.covers1624.lwjglagent.StubbedMethod;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;

public class DrawableGL implements Drawable {

    protected final long window;
    private @Nullable GLCapabilities capabilities;

    public DrawableGL(long window) {
        this.window = window;
    }

    @Override
    public boolean isCurrent() {
        return GLFW.glfwGetCurrentContext() == window;
    }

    @Override
    public void makeCurrent() {
        GLFW.glfwMakeContextCurrent(window);
        if (capabilities == null) {
            capabilities = GL.createCapabilities();
        } else {
            GL.setCapabilities(capabilities);
        }
    }

    @Override
    public void releaseContext() {
        GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);
    }

    @Override
    public void destroy() {
        glfwDestroyWindow(window);
    }

    @Override
    public void setCLSharingProperties(PointerBuffer properties) {
        throw new StubbedMethod();
    }

    long createSharedContext() {
        return GLFW.glfwCreateWindow(1, 1, "shared context", MemoryUtil.NULL, window);
    }
}
