/*
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lwjgl.input;

import net.covers1624.lwjglagent.StubbedMethod;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.LWJGLException;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.Display;
import org.lwjgl.system.MemoryUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

/**
 * <br>
 * A raw Mouse interface. This can be used to poll the current state of the
 * mouse buttons, and determine the mouse movement delta since the last poll.
 * <p>
 * n buttons supported, n being a native limit. A scrolly wheel is also
 * supported, if one such is available. Movement is reported as delta from
 * last position or as an absolute position. If the window has been created
 * the absolute position will be clamped to 0 - width | height.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision$
 * $Id$
 */
public class Mouse {

    private static final String[] buttonName = new String[GLFW_MOUSE_BUTTON_LAST + 1];
    private static final Map<String, Integer> buttonMap = new HashMap<String, Integer>(GLFW_MOUSE_BUTTON_LAST + 1);

    private static long window;
    private static @Nullable GLFWMouseButtonCallback buttonCallback;
    private static @Nullable GLFWCursorPosCallback cursorPosCallback;
    private static @Nullable GLFWScrollCallback scrollCallback;
    private static @Nullable GLFWCursorEnterCallback cursorEnterCallback;

    private static final boolean[] buttons = new boolean[GLFW_MOUSE_BUTTON_LAST + 1];
    private static final int MAX_EVENTS = 200;
    private static final LinkedList<MouseEvent> queue = new LinkedList<>();
    private static final MouseEvent currentEvent = new MouseEvent();

    private static boolean insideWindow = false;

    private static int x;
    private static int y;
    private static int dx;
    private static int dy;
    private static int dwheel;

    private static int grab_x;
    private static int grab_y;

    private static boolean isGrabbed;

    private static boolean clipMouseCoordinatesToWindow = !Boolean.getBoolean("org.lwjgl.input.Mouse.allowNegativeMouseCoords");

    static {
        for (int i = 0; i < buttons.length; i++) {
            buttonName[i] = "BUTTON" + i;
            buttonMap.put(buttonName[i], i);
        }
    }

    private Mouse() {
    }

    public static Cursor getNativeCursor() {
        throw new StubbedMethod();
    }

    public static Cursor setNativeCursor(Cursor cursor) throws LWJGLException {
        throw new StubbedMethod();
    }

    public static boolean isClipMouseCoordinatesToWindow() {
        return clipMouseCoordinatesToWindow;
    }

    public static void setClipMouseCoordinatesToWindow(boolean clip) {
        clipMouseCoordinatesToWindow = clip;
    }

    public static void setCursorPosition(int new_x, int new_y) {
        if (!isCreated()) throw new IllegalStateException("Mouse is not created");

        x = currentEvent.x = new_x;
        y = currentEvent.y = new_y;
        if (!isGrabbed()) {
            glfwSetCursorPos(window, x, y);
        } else {
            grab_x = new_x;
            grab_y = new_y;
        }
    }

    private static int last_x;
    private static int last_y;

    public static void create() throws LWJGLException {
        if (!Display.isCreated()) throw new IllegalStateException("Display must be created.");

        window = glfwGetCurrentContext();
        if (window == MemoryUtil.NULL) throw new LWJGLException("No glfw context?");
        if (glfwRawMouseMotionSupported()) {
            glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
        }
        buttonCallback = glfwSetMouseButtonCallback(window, (window1, button, action, mods) -> {
            if (button >= buttons.length) return;

            buttons[button] = action == GLFW_PRESS;
            addPressEvent(button, buttons[button], 0);
        });
        scrollCallback = glfwSetScrollCallback(window, (window1, xoffset, yoffset) -> {
            dwheel += (int) yoffset;
            addPressEvent(-1, false, (int) yoffset);
        });
        cursorPosCallback = glfwSetCursorPosCallback(window, (w, xpos, ypos) -> {
            x = (int) xpos;
            y = Display.getHeight() - 1 - (int) ypos;
            int dx = x - last_x;
            int dy = y - last_y;
            last_x = x;
            last_y = y;
            // Mouse.dx and Mouse.dy are cumulative till queried.
            Mouse.dx += dx;
            Mouse.dy += dy;
            addEvent(-1, false, x, y, dx, dy, 0);
            if (clipMouseCoordinatesToWindow) {
                x = Math.min(Display.getWidth() - 1, Math.max(0, x));
                y = Math.min(Display.getHeight() - 1, Math.max(0, y));
            }
        });
        cursorEnterCallback = glfwSetCursorEnterCallback(window, (window1, entered) -> insideWindow = entered);
        setGrabbed(isGrabbed);
    }

    private static void addPressEvent(int button, boolean state, int dwheel) {
        if (isGrabbed) {
            addEvent(button, state, x, y, 0, 0, dwheel);
        } else {
            addEvent(button, state, x, y, dx, dy, dwheel);
        }
    }

    private static void addEvent(int button, boolean state, int x, int y, int dx, int dy, int dwheel) {
        if (queue.size() == MAX_EVENTS) {
            queue.removeFirst();
        }

        queue.addLast(new MouseEvent(button, state, x, y, dx, dy, dwheel));
    }

    public static boolean isCreated() {
        return window != MemoryUtil.NULL;
    }

    public static void destroy() {
        if (!isCreated()) return;
        window = MemoryUtil.NULL;

        if (buttonCallback != null) buttonCallback.free();
        buttonCallback = null;
        if (cursorPosCallback != null) cursorPosCallback.free();
        cursorPosCallback = null;
        if (scrollCallback != null) scrollCallback.free();
        scrollCallback = null;
        if (cursorEnterCallback != null) cursorEnterCallback.free();
        cursorEnterCallback = null;

        Arrays.fill(buttons, false);
        queue.clear();
        currentEvent.reset(false);
    }

    public static void poll() {
    }

    public static boolean isButtonDown(int button) {
        if (!isCreated()) throw new IllegalStateException("Mouse must be created before you can poll the button state");
        if (button >= buttons.length || button < 0) return false;

        return buttons[button];
    }

    public static String getButtonName(int button) {
        if (button >= buttonName.length || button < 0) return null;

        return buttonName[button];
    }

    public static int getButtonIndex(String buttonName) {
        Integer ret = buttonMap.get(buttonName);
        if (ret == null) return -1;

        return ret;
    }

    public static boolean next() {
        if (!isCreated()) throw new IllegalStateException("Mouse must be created before you can read events");
        if (queue.isEmpty()) return false;

        currentEvent.set(queue.removeFirst());
        currentEvent.clip();
        return true;
    }

    public static int getEventButton() {
        return currentEvent.button;
    }

    public static boolean getEventButtonState() {
        return currentEvent.state;
    }

    public static int getEventDX() {
        return currentEvent.dx;
    }

    public static int getEventDY() {
        return currentEvent.dy;
    }

    public static int getEventX() {
        return currentEvent.x;
    }

    public static int getEventY() {
        return currentEvent.y;
    }

    public static int getEventDWheel() {
        return currentEvent.dwheel;
    }

    public static long getEventNanoseconds() {
        return currentEvent.nanos;
    }

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

    public static int getDX() {
        int result = dx;
        dx = 0;
        return result;
    }

    public static int getDY() {
        int result = dy;
        dy = 0;
        return result;
    }

    public static int getDWheel() {
        int result = dwheel;
        dwheel = 0;
        return result;
    }

    public static int getButtonCount() {
        return buttons.length;
    }

    public static boolean hasWheel() {
        return true;
    }

    public static boolean isGrabbed() {
        return isGrabbed;
    }

    public static void setGrabbed(boolean grab) {
        boolean grabbed = isGrabbed;
        isGrabbed = grab;
        if (isCreated()) {
            if (grab && !grabbed) {
                // store location mouse was grabbed
                grab_x = x;
                grab_y = y;
            } else if (!grab && grabbed) {
                // move mouse back to location it was grabbed before ungrabbing
                glfwSetCursorPos(window, grab_x, grab_y);
            }

            glfwSetInputMode(window, GLFW_CURSOR, grab ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
            queue.clear();
            currentEvent.reset(true);
            dx = dy = dwheel = 0;
        }
    }

    public static void updateCursor() {
        throw new StubbedMethod();
    }

    public static boolean isInsideWindow() {
        return insideWindow;
    }

    private static final class MouseEvent {

        public int button;
        public boolean state;
        public int x;
        public int y;
        public int dx;
        public int dy;
        public int dwheel;
        public long nanos;

        private MouseEvent() { }

        private MouseEvent(int button, boolean state, int x, int y, int dx, int dy, int dwheel) {
            this.button = button;
            this.state = state;
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.dwheel = dwheel;
            nanos = System.nanoTime();
        }

        private void set(MouseEvent other) {
            button = other.button;
            state = other.state;
            x = other.x;
            y = other.y;
            dx = other.dx;
            dy = other.dy;
            dwheel = other.dwheel;
            nanos = other.nanos;
        }

        private void reset(boolean movementOnly) {
            if (movementOnly) {
                button = -1;
                state = false;
            }
            x = 0;
            y = 0;
            dx = 0;
            dy = 0;
            dwheel = 0;
        }

        private void clip() {
            if (clipMouseCoordinatesToWindow) {
                x = Math.min(Display.getWidth() - 1, Math.max(0, x));
                y = Math.min(Display.getHeight() - 1, Math.max(0, y));
            }
        }
    }
}
