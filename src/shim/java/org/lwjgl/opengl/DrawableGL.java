package org.lwjgl.opengl;

import net.covers1624.lwjglagent.StubbedMethod;
import org.lwjgl.LWJGLException;
import org.lwjgl.PointerBuffer;

// TODO stub.
public class DrawableGL implements Drawable {

    protected final long window;

    public DrawableGL(long window) {
        this.window = window;
    }

    @Override
    public boolean isCurrent() throws LWJGLException {
        throw new StubbedMethod();
    }

    @Override
    public void makeCurrent() throws LWJGLException {
        throw new StubbedMethod();
    }

    @Override
    public void releaseContext() throws LWJGLException {
        throw new StubbedMethod();
    }

    @Override
    public void destroy() {
        throw new StubbedMethod();
    }

    @Override
    public void setCLSharingProperties(PointerBuffer properties) throws LWJGLException {
        throw new StubbedMethod();
    }
}
