package org.lwjgl.opengl;

// TODO stub.
public class SharedDrawable extends DrawableGL {

    public SharedDrawable(Drawable parent) {
        super(((DrawableGL) parent).window);
    }
}
