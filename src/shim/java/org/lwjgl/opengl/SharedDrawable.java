package org.lwjgl.opengl;

public class SharedDrawable extends DrawableGL {

    public SharedDrawable(Drawable parent) {
        super(((DrawableGL) parent).createSharedContext());
    }
}
