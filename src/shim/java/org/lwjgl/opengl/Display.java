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
package org.lwjgl.opengl;

import net.covers1624.lwjglagent.PartialStubbedMethod;
import net.covers1624.lwjglagent.StubbedMethod;
import net.covers1624.quack.collection.FastStream;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.LWJGLException;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * This is the abstract class for a Display in LWJGL. LWJGL displays have some
 * peculiar characteristics:
 *
 * - the display may be closeable by the user or operating system, and may be minimized
 * by the user or operating system
 * - only one display may ever be open at once
 * - the operating system may or may not be able to do fullscreen or windowed displays.
 *
 * @author foo
 */

public class Display {

    private static final Logger LOGGER = LoggerFactory.getLogger(Display.class);

    private static final DisplayMode[] EMPTY_DISPLAY_MODES = new DisplayMode[0];

    private static DisplayMode displayMode;
    private static boolean resizable;
    private static String title = "Game";

    private static long window = MemoryUtil.NULL;

    private static int width;
    private static int height;

    private static @Nullable GLFWWindowSizeCallback resizeCallback;
    private static boolean wasResized;
    private static double lastDrawTime = Double.MIN_VALUE;

    public static Drawable getDrawable() {
        throw new StubbedMethod();
    }

    public static DisplayMode[] getAvailableDisplayModes() {
        long monitor = glfwGetPrimaryMonitor();
        if (monitor == MemoryUtil.NULL) return EMPTY_DISPLAY_MODES;

        GLFWVidMode.Buffer buffer = glfwGetVideoModes(monitor);
        if (buffer == null) return EMPTY_DISPLAY_MODES;

        return FastStream.of(buffer)
                .map(Display::getMode)
                .toArray(EMPTY_DISPLAY_MODES);
    }

    // TODO this is actually initialized at Display init. We should do that too. Eventually.
    public static @Nullable DisplayMode getDesktopDisplayMode() {
        long monitor = glfwGetPrimaryMonitor();
        if (monitor == MemoryUtil.NULL) return null;

        GLFWVidMode mode = glfwGetVideoMode(monitor);
        if (mode == null) return null;

        return getMode(mode);
    }

    private static DisplayMode getMode(GLFWVidMode mode) {
        return new DisplayMode(mode.width(), mode.height(), mode.redBits() + mode.blueBits() + mode.greenBits(), mode.refreshRate());
    }

    public static DisplayMode getDisplayMode() {
        return displayMode;
    }

    public static void setDisplayMode(DisplayMode mode) throws LWJGLException {
        displayMode = mode;
        if (isCreated()) {
            throw new PartialStubbedMethod();
        }
    }

    public static void setDisplayConfiguration(float gamma, float brightness, float contrast) throws LWJGLException {
        throw new StubbedMethod();
    }

    public static void sync(int fps) {
        double nextTime = lastDrawTime + 1.0D / (double) fps;

        double currentTime;
        for (currentTime = GLFW.glfwGetTime(); currentTime < nextTime; currentTime = GLFW.glfwGetTime()) {
            GLFW.glfwWaitEventsTimeout(nextTime - currentTime);
        }

        lastDrawTime = currentTime;
    }

    public static String getTitle() {
        throw new StubbedMethod();
    }

    public static Canvas getParent() {
        throw new StubbedMethod();
    }

    public static void setParent(Canvas canvas) throws LWJGLException {
        throw new StubbedMethod();
    }

    public static void setFullscreen(boolean fullscreen) throws LWJGLException {
        throw new StubbedMethod();
    }

    public static void setDisplayModeAndFullscreen(DisplayMode mode) throws LWJGLException {
        throw new StubbedMethod();
    }

    public static boolean isFullscreen() {
        throw new StubbedMethod();
    }

    public static void setTitle(String newTitle) {
        title = newTitle;
        if (isCreated()) {
            throw new PartialStubbedMethod();
        }
    }

    public static boolean isCloseRequested() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public static boolean isVisible() {
        return glfwGetWindowAttrib(window, GLFW_VISIBLE) == GLFW_TRUE;
    }

    public static boolean isActive() {
        return glfwGetWindowAttrib(window, GLFW_FOCUSED) == GLFW_TRUE;
    }

    public static boolean isDirty() {
        throw new StubbedMethod();
    }

    public static void processMessages() {
        throw new StubbedMethod();
    }

    public static void swapBuffers() throws LWJGLException {
        throw new StubbedMethod();
    }

    public static void update() {
        wasResized = false;
        glfwPollEvents();
        Mouse.poll();

        glfwSwapBuffers(window);
    }

    public static void update(boolean processMessages) {
        throw new StubbedMethod();
    }

    public static void releaseContext() throws LWJGLException {
        throw new StubbedMethod();
    }

    public static boolean isCurrent() throws LWJGLException {
        throw new StubbedMethod();
    }

    public static void makeCurrent() throws LWJGLException {
        throw new StubbedMethod();
    }

    public static void create() throws LWJGLException {
        throw new StubbedMethod();
    }

    public static void create(PixelFormat pixel_format) throws LWJGLException {
        if (isCreated()) throw new LWJGLException("Only one LWJGL context may be instantiated at any one time.");

        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new LWJGLException("Failed to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
        window = GLFW.glfwCreateWindow(displayMode.getWidth(), displayMode.getHeight(), title, MemoryUtil.NULL, MemoryUtil.NULL);
        width = displayMode.getWidth();
        height = displayMode.getHeight();
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        Mouse.create();
        Keyboard.create();

        resizeCallback = GLFWWindowSizeCallback.create(Display::onResized);
        glfwSetWindowSizeCallback(window, resizeCallback);

        GLFW.glfwShowWindow(window);
    }

    public static void create(PixelFormat pixel_format, Drawable shared_drawable) throws LWJGLException {
        throw new StubbedMethod();
    }

    public static void create(PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
        throw new StubbedMethod();
    }

    public static void create(PixelFormat pixel_format, Drawable shared_drawable, ContextAttribs attribs) throws LWJGLException {
        throw new StubbedMethod();
    }

    public static void create(PixelFormatLWJGL pixel_format) throws LWJGLException {
        throw new StubbedMethod();
    }

    public static void create(PixelFormatLWJGL pixel_format, Drawable shared_drawable) throws LWJGLException {
        throw new StubbedMethod();
    }

    public static void create(PixelFormatLWJGL pixel_format, org.lwjgl.opengles.ContextAttribs attribs) throws LWJGLException {
        throw new StubbedMethod();
    }

    public static void create(PixelFormatLWJGL pixel_format, Drawable shared_drawable, org.lwjgl.opengles.ContextAttribs attribs) throws LWJGLException {
        throw new StubbedMethod();
    }

    public static void setInitialBackground(float red, float green, float blue) {
        throw new StubbedMethod();
    }

    public static void destroy() {
        resizeCallback.free();
        Mouse.destroy();
        Keyboard.destroy();

        glfwDestroyWindow(window);
        glfwTerminate();
        // noinspection EmptyTryBlock
        try (GLFWErrorCallback prev = glfwSetErrorCallback(null)) { }
    }

    public static boolean isCreated() {
        return window != MemoryUtil.NULL;
    }

    public static void setSwapInterval(int value) {
        throw new StubbedMethod();
    }

    public static void setVSyncEnabled(boolean sync) {
        glfwSwapInterval(sync ? 1 : 0);
    }

    public static void setLocation(int new_x, int new_y) {
        throw new StubbedMethod();
    }

    public static String getAdapter() {
        throw new StubbedMethod();
    }

    public static String getVersion() {
        throw new StubbedMethod();
    }

    public static int setIcon(ByteBuffer[] icons) {
        // TODO no-op for now.
        return 0;
    }

    public static void setResizable(boolean resizable) {
        Display.resizable = resizable;
        if (isCreated()) {
            throw new PartialStubbedMethod();
        }
    }

    public static boolean isResizable() {
        return resizable;
    }

    public static boolean wasResized() {
        return wasResized;
    }

    public static int getX() {
        throw new StubbedMethod();
    }

    public static int getY() {
        throw new StubbedMethod();
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static float getPixelScaleFactor() {
        throw new StubbedMethod();
    }

    private static void onResized(long window, int width, int height) {
        if (window != Display.window) return;

        wasResized = true;
        Display.width = width;
        Display.height = height;
        displayMode = new DisplayMode(width, height, displayMode.getBitsPerPixel(), displayMode.getFrequency());
    }
}
