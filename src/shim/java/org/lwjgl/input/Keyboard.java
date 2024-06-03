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

import net.covers1624.quack.collection.FastStream;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.LWJGLException;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.Display;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

/**
 * <br>
 * A raw Keyboard interface. This can be used to poll the current state of the
 * keys, or read all the keyboard presses / releases since the last read.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision$
 * $Id$
 */
public class Keyboard {

    private static final Logger LOGGER = LoggerFactory.getLogger(Keyboard.class);

    public static final int CHAR_NONE = '\0';
    public static final int KEY_NONE = 0x00;
    public static final int KEY_ESCAPE = 0x01;
    public static final int KEY_1 = 0x02;
    public static final int KEY_2 = 0x03;
    public static final int KEY_3 = 0x04;
    public static final int KEY_4 = 0x05;
    public static final int KEY_5 = 0x06;
    public static final int KEY_6 = 0x07;
    public static final int KEY_7 = 0x08;
    public static final int KEY_8 = 0x09;
    public static final int KEY_9 = 0x0A;
    public static final int KEY_0 = 0x0B;
    public static final int KEY_MINUS = 0x0C; /* - on main keyboard */
    public static final int KEY_EQUALS = 0x0D;
    public static final int KEY_BACK = 0x0E; /* backspace */
    public static final int KEY_TAB = 0x0F;
    public static final int KEY_Q = 0x10;
    public static final int KEY_W = 0x11;
    public static final int KEY_E = 0x12;
    public static final int KEY_R = 0x13;
    public static final int KEY_T = 0x14;
    public static final int KEY_Y = 0x15;
    public static final int KEY_U = 0x16;
    public static final int KEY_I = 0x17;
    public static final int KEY_O = 0x18;
    public static final int KEY_P = 0x19;
    public static final int KEY_LBRACKET = 0x1A;
    public static final int KEY_RBRACKET = 0x1B;
    public static final int KEY_RETURN = 0x1C; /* Enter on main keyboard */
    public static final int KEY_LCONTROL = 0x1D;
    public static final int KEY_A = 0x1E;
    public static final int KEY_S = 0x1F;
    public static final int KEY_D = 0x20;
    public static final int KEY_F = 0x21;
    public static final int KEY_G = 0x22;
    public static final int KEY_H = 0x23;
    public static final int KEY_J = 0x24;
    public static final int KEY_K = 0x25;
    public static final int KEY_L = 0x26;
    public static final int KEY_SEMICOLON = 0x27;
    public static final int KEY_APOSTROPHE = 0x28;
    public static final int KEY_GRAVE = 0x29; /* accent grave */
    public static final int KEY_LSHIFT = 0x2A;
    public static final int KEY_BACKSLASH = 0x2B;
    public static final int KEY_Z = 0x2C;
    public static final int KEY_X = 0x2D;
    public static final int KEY_C = 0x2E;
    public static final int KEY_V = 0x2F;
    public static final int KEY_B = 0x30;
    public static final int KEY_N = 0x31;
    public static final int KEY_M = 0x32;
    public static final int KEY_COMMA = 0x33;
    public static final int KEY_PERIOD = 0x34; /* . on main keyboard */
    public static final int KEY_SLASH = 0x35; /* / on main keyboard */
    public static final int KEY_RSHIFT = 0x36;
    public static final int KEY_MULTIPLY = 0x37; /* * on numeric keypad */
    public static final int KEY_LMENU = 0x38; /* left Alt */
    public static final int KEY_SPACE = 0x39;
    public static final int KEY_CAPITAL = 0x3A;
    public static final int KEY_F1 = 0x3B;
    public static final int KEY_F2 = 0x3C;
    public static final int KEY_F3 = 0x3D;
    public static final int KEY_F4 = 0x3E;
    public static final int KEY_F5 = 0x3F;
    public static final int KEY_F6 = 0x40;
    public static final int KEY_F7 = 0x41;
    public static final int KEY_F8 = 0x42;
    public static final int KEY_F9 = 0x43;
    public static final int KEY_F10 = 0x44;
    public static final int KEY_NUMLOCK = 0x45;
    public static final int KEY_SCROLL = 0x46; /* Scroll Lock */
    public static final int KEY_NUMPAD7 = 0x47;
    public static final int KEY_NUMPAD8 = 0x48;
    public static final int KEY_NUMPAD9 = 0x49;
    public static final int KEY_SUBTRACT = 0x4A; /* - on numeric keypad */
    public static final int KEY_NUMPAD4 = 0x4B;
    public static final int KEY_NUMPAD5 = 0x4C;
    public static final int KEY_NUMPAD6 = 0x4D;
    public static final int KEY_ADD = 0x4E; /* + on numeric keypad */
    public static final int KEY_NUMPAD1 = 0x4F;
    public static final int KEY_NUMPAD2 = 0x50;
    public static final int KEY_NUMPAD3 = 0x51;
    public static final int KEY_NUMPAD0 = 0x52;
    public static final int KEY_DECIMAL = 0x53; /* . on numeric keypad */
    public static final int KEY_F11 = 0x57;
    public static final int KEY_F12 = 0x58;
    public static final int KEY_F13 = 0x64; /*                     (NEC PC98) */
    public static final int KEY_F14 = 0x65; /*                     (NEC PC98) */
    public static final int KEY_F15 = 0x66; /*                     (NEC PC98) */
    public static final int KEY_F16 = 0x67; /* Extended Function keys - (Mac) */
    public static final int KEY_F17 = 0x68;
    public static final int KEY_F18 = 0x69;
    public static final int KEY_KANA = 0x70; /* (Japanese keyboard)            */
    public static final int KEY_F19 = 0x71; /* Extended Function keys - (Mac) */
    public static final int KEY_CONVERT = 0x79; /* (Japanese keyboard)            */
    public static final int KEY_NOCONVERT = 0x7B; /* (Japanese keyboard)            */
    public static final int KEY_YEN = 0x7D; /* (Japanese keyboard)            */
    public static final int KEY_NUMPADEQUALS = 0x8D; /* = on numeric keypad (NEC PC98) */
    public static final int KEY_CIRCUMFLEX = 0x90; /* (Japanese keyboard)            */
    public static final int KEY_AT = 0x91; /*                     (NEC PC98) */
    public static final int KEY_COLON = 0x92; /*                     (NEC PC98) */
    public static final int KEY_UNDERLINE = 0x93; /*                     (NEC PC98) */
    public static final int KEY_KANJI = 0x94; /* (Japanese keyboard)            */
    public static final int KEY_STOP = 0x95; /*                     (NEC PC98) */
    public static final int KEY_AX = 0x96; /*                     (Japan AX) */
    public static final int KEY_UNLABELED = 0x97; /*                        (J3100) */
    public static final int KEY_NUMPADENTER = 0x9C; /* Enter on numeric keypad */
    public static final int KEY_RCONTROL = 0x9D;
    public static final int KEY_SECTION = 0xA7; /* Section symbol (Mac) */
    public static final int KEY_NUMPADCOMMA = 0xB3; /* , on numeric keypad (NEC PC98) */
    public static final int KEY_DIVIDE = 0xB5; /* / on numeric keypad */
    public static final int KEY_SYSRQ = 0xB7;
    public static final int KEY_RMENU = 0xB8; /* right Alt */
    public static final int KEY_FUNCTION = 0xC4; /* Function (Mac) */
    public static final int KEY_PAUSE = 0xC5; /* Pause */
    public static final int KEY_HOME = 0xC7; /* Home on arrow keypad */
    public static final int KEY_UP = 0xC8; /* UpArrow on arrow keypad */
    public static final int KEY_PRIOR = 0xC9; /* PgUp on arrow keypad */
    public static final int KEY_LEFT = 0xCB; /* LeftArrow on arrow keypad */
    public static final int KEY_RIGHT = 0xCD; /* RightArrow on arrow keypad */
    public static final int KEY_END = 0xCF; /* End on arrow keypad */
    public static final int KEY_DOWN = 0xD0; /* DownArrow on arrow keypad */
    public static final int KEY_NEXT = 0xD1; /* PgDn on arrow keypad */
    public static final int KEY_INSERT = 0xD2; /* Insert on arrow keypad */
    public static final int KEY_DELETE = 0xD3; /* Delete on arrow keypad */
    public static final int KEY_CLEAR = 0xDA; /* Clear key (Mac) */
    public static final int KEY_LMETA = 0xDB; /* Left Windows/Option key */
    public static final int KEY_RMETA = 0xDC; /* Right Windows/Option key */
    public static final int KEY_APPS = 0xDD; /* AppMenu key */
    public static final int KEY_POWER = 0xDE;
    public static final int KEY_SLEEP = 0xDF;

    public static final int KEYBOARD_SIZE = 256;

    private static final String[] keyName = new String[KEYBOARD_SIZE];
    private static final Map<String, Integer> keyMap = new HashMap<String, Integer>(253);
    private static int numKeys;

    static {
        try {
            for (Field field : Keyboard.class.getFields()) {
                if (!Modifier.isStatic(field.getModifiers())) continue;
                if (!Modifier.isPublic(field.getModifiers())) continue;
                if (!Modifier.isFinal(field.getModifiers())) continue;
                if (!field.getType().equals(int.class)) continue;
                if (!field.getName().startsWith("KEY_")) continue;

                int key = field.getInt(null);
                String name = field.getName().substring(4);
                keyName[key] = name;
                keyMap.put(name, key);
                numKeys++;
            }
        } catch (ReflectiveOperationException ex) {
            LOGGER.error("Failed to load Keyboard name <-> id mappings.", ex);
        }
    }

    private static boolean repeatKeys;
    private static long window;
    private static @Nullable GLFWKeyCallback keyCallback;
    private static @Nullable GLFWCharCallback charCallback;
    private static final boolean[] keys = new boolean[KEYBOARD_SIZE];
    private static final int MAX_EVENTS = 200;
    private static final LinkedList<KeyEvent> events = new LinkedList<>();
    private static final KeyEvent currentEvent = new KeyEvent();

    private Keyboard() {
    }

    public static void create() throws LWJGLException {
        if (!Display.isCreated()) throw new IllegalStateException("Display must be created.");

        window = glfwGetCurrentContext();
        if (window == MemoryUtil.NULL) throw new LWJGLException("No glfw context?");

        keyCallback = glfwSetKeyCallback(window, ((window1, key, scancode, action, mods) -> {
            int lwjglKey = mapKey(key);
            if (action == GLFW_PRESS) {
                keys[lwjglKey] = true;
            } else if (action == GLFW_RELEASE) {
                keys[lwjglKey] = false;
            }
            addEvent(new KeyEvent(0, lwjglKey, keys[lwjglKey], action == GLFW_REPEAT));
        }));
        charCallback = glfwSetCharCallback(window, (window1, codepoint) -> {
            addEvent(new KeyEvent(0, -1, true, false));
        });
    }

    private static void addEvent(KeyEvent event) {
        if (events.size() == MAX_EVENTS) {
            events.removeFirst();
        }
        events.addLast(event);
    }

    public static boolean isCreated() {
        return window != MemoryUtil.NULL;
    }

    public static void destroy() {
        if (!isCreated()) return;

        window = MemoryUtil.NULL;
        if (keyCallback != null) keyCallback.free();
        keyCallback = null;
        if (charCallback != null) charCallback.free();
        charCallback = null;

        events.clear();
        Arrays.fill(keys, false);
        currentEvent.reset();
    }

    public static void poll() {
        // Do nothing, glfw polling from Display.update pushes data into us.
    }

    public static boolean isKeyDown(int key) {
        if (!isCreated()) throw new IllegalStateException("Keyboard must be created before you can query key state");
        return keys[key];
    }

    public static synchronized String getKeyName(int key) {
        return keyName[key];
    }

    public static synchronized int getKeyIndex(String keyName) {
        Integer ret = keyMap.get(keyName);
        if (ret != null) return ret;

        return KEY_NONE;
    }

    public static int getNumKeyboardEvents() {
        if (!isCreated()) throw new IllegalStateException("Keyboard must be created before you can read events");
        return FastStream.of(events)
                .filter(e -> !e.repeat || repeatKeys)
                .count();
    }

    public static boolean next() {
        if (!isCreated()) throw new IllegalStateException("Keyboard must be created before you can read events");

        while (!events.isEmpty()) {
            KeyEvent next = events.removeFirst();
            if (next.repeat && !repeatKeys) continue;

            currentEvent.set(next);
            return true;
        }
        return false;
    }

    public static void enableRepeatEvents(boolean enable) {
        repeatKeys = enable;
    }

    public static boolean areRepeatEventsEnabled() {
        return repeatKeys;
    }

    public static int getKeyCount() {
        return numKeys;
    }

    public static char getEventCharacter() {
        return (char) currentEvent.character;
    }

    public static int getEventKey() {
        return currentEvent.key;
    }

    public static boolean getEventKeyState() {
        return currentEvent.state;
    }

    public static long getEventNanoseconds() {
        return currentEvent.nanos;
    }

    public static boolean isRepeatEvent() {
        return currentEvent.repeat;
    }

    private static final class KeyEvent {

        private int character;
        private int key;
        private boolean state;
        private long nanos;
        private boolean repeat;

        public KeyEvent() {
        }

        public KeyEvent(int character, int key, boolean state, boolean repeat) {
            this.character = character;
            this.key = key;
            this.state = state;
            this.repeat = repeat;
            this.nanos = System.nanoTime();
        }

        private void set(KeyEvent other) {
            character = other.character;
            key = other.key;
            state = other.state;
            nanos = other.nanos;
            repeat = other.repeat;
        }

        private void reset() {
            character = 0;
            key = 0;
            state = false;
            repeat = false;
        }
    }

    private static int mapKey(int key) {
        if (key < KEY_MAP.length && key >= 0) {
            return KEY_MAP[key];
        }
        return KEY_NONE;
    }

    // GLFW -> LWJGL2
    private static final int[] KEY_MAP = new int[GLFW_KEY_LAST + 1];

    static {
        Arrays.fill(KEY_MAP, KEY_NONE);
        KEY_MAP[GLFW_KEY_SPACE] = KEY_SPACE;
        KEY_MAP[GLFW_KEY_APOSTROPHE] = KEY_APOSTROPHE;
        KEY_MAP[GLFW_KEY_COMMA] = KEY_COMMA;
        KEY_MAP[GLFW_KEY_MINUS] = KEY_MINUS;
        KEY_MAP[GLFW_KEY_PERIOD] = KEY_PERIOD;
        KEY_MAP[GLFW_KEY_SLASH] = KEY_SLASH;
        KEY_MAP[GLFW_KEY_0] = KEY_0;
        KEY_MAP[GLFW_KEY_1] = KEY_1;
        KEY_MAP[GLFW_KEY_2] = KEY_2;
        KEY_MAP[GLFW_KEY_3] = KEY_3;
        KEY_MAP[GLFW_KEY_4] = KEY_4;
        KEY_MAP[GLFW_KEY_5] = KEY_5;
        KEY_MAP[GLFW_KEY_6] = KEY_6;
        KEY_MAP[GLFW_KEY_7] = KEY_7;
        KEY_MAP[GLFW_KEY_8] = KEY_8;
        KEY_MAP[GLFW_KEY_9] = KEY_9;
        KEY_MAP[GLFW_KEY_SEMICOLON] = KEY_SEMICOLON;
        KEY_MAP[GLFW_KEY_EQUAL] = KEY_EQUALS;
        KEY_MAP[GLFW_KEY_A] = KEY_A;
        KEY_MAP[GLFW_KEY_B] = KEY_B;
        KEY_MAP[GLFW_KEY_C] = KEY_C;
        KEY_MAP[GLFW_KEY_D] = KEY_D;
        KEY_MAP[GLFW_KEY_E] = KEY_E;
        KEY_MAP[GLFW_KEY_F] = KEY_F;
        KEY_MAP[GLFW_KEY_G] = KEY_G;
        KEY_MAP[GLFW_KEY_H] = KEY_H;
        KEY_MAP[GLFW_KEY_I] = KEY_I;
        KEY_MAP[GLFW_KEY_J] = KEY_J;
        KEY_MAP[GLFW_KEY_K] = KEY_K;
        KEY_MAP[GLFW_KEY_L] = KEY_L;
        KEY_MAP[GLFW_KEY_M] = KEY_M;
        KEY_MAP[GLFW_KEY_N] = KEY_N;
        KEY_MAP[GLFW_KEY_O] = KEY_O;
        KEY_MAP[GLFW_KEY_P] = KEY_P;
        KEY_MAP[GLFW_KEY_Q] = KEY_Q;
        KEY_MAP[GLFW_KEY_R] = KEY_R;
        KEY_MAP[GLFW_KEY_S] = KEY_S;
        KEY_MAP[GLFW_KEY_T] = KEY_T;
        KEY_MAP[GLFW_KEY_U] = KEY_U;
        KEY_MAP[GLFW_KEY_V] = KEY_V;
        KEY_MAP[GLFW_KEY_W] = KEY_W;
        KEY_MAP[GLFW_KEY_X] = KEY_X;
        KEY_MAP[GLFW_KEY_Y] = KEY_Y;
        KEY_MAP[GLFW_KEY_Z] = KEY_Z;
        KEY_MAP[GLFW_KEY_LEFT_BRACKET] = KEY_LBRACKET;
        KEY_MAP[GLFW_KEY_BACKSLASH] = KEY_BACKSLASH;
        KEY_MAP[GLFW_KEY_RIGHT_BRACKET] = KEY_RBRACKET;
        KEY_MAP[GLFW_KEY_GRAVE_ACCENT] = KEY_GRAVE;
        KEY_MAP[GLFW_KEY_ESCAPE] = KEY_ESCAPE;
        KEY_MAP[GLFW_KEY_ENTER] = KEY_RETURN;
        KEY_MAP[GLFW_KEY_TAB] = KEY_TAB;
        KEY_MAP[GLFW_KEY_BACKSPACE] = KEY_BACK;
        KEY_MAP[GLFW_KEY_INSERT] = KEY_INSERT;
        KEY_MAP[GLFW_KEY_DELETE] = KEY_DELETE;
        KEY_MAP[GLFW_KEY_RIGHT] = KEY_RIGHT;
        KEY_MAP[GLFW_KEY_LEFT] = KEY_LEFT;
        KEY_MAP[GLFW_KEY_DOWN] = KEY_DOWN;
        KEY_MAP[GLFW_KEY_UP] = KEY_UP;
        KEY_MAP[GLFW_KEY_PAGE_UP] = KEY_PRIOR;
        KEY_MAP[GLFW_KEY_PAGE_DOWN] = KEY_NEXT;
        KEY_MAP[GLFW_KEY_HOME] = KEY_HOME;
        KEY_MAP[GLFW_KEY_END] = KEY_END;
        KEY_MAP[GLFW_KEY_CAPS_LOCK] = KEY_CAPITAL;
        KEY_MAP[GLFW_KEY_SCROLL_LOCK] = KEY_SCROLL;
        KEY_MAP[GLFW_KEY_NUM_LOCK] = KEY_NUMLOCK;
        KEY_MAP[GLFW_KEY_PAUSE] = KEY_PAUSE;
        KEY_MAP[GLFW_KEY_F1] = KEY_F1;
        KEY_MAP[GLFW_KEY_F2] = KEY_F2;
        KEY_MAP[GLFW_KEY_F3] = KEY_F3;
        KEY_MAP[GLFW_KEY_F4] = KEY_F4;
        KEY_MAP[GLFW_KEY_F5] = KEY_F5;
        KEY_MAP[GLFW_KEY_F6] = KEY_F6;
        KEY_MAP[GLFW_KEY_F7] = KEY_F7;
        KEY_MAP[GLFW_KEY_F8] = KEY_F8;
        KEY_MAP[GLFW_KEY_F9] = KEY_F9;
        KEY_MAP[GLFW_KEY_F10] = KEY_F10;
        KEY_MAP[GLFW_KEY_F11] = KEY_F11;
        KEY_MAP[GLFW_KEY_F12] = KEY_F12;
        KEY_MAP[GLFW_KEY_F13] = KEY_F13;
        KEY_MAP[GLFW_KEY_F14] = KEY_F14;
        KEY_MAP[GLFW_KEY_F15] = KEY_F15;
        KEY_MAP[GLFW_KEY_F16] = KEY_F16;
        KEY_MAP[GLFW_KEY_F17] = KEY_F17;
        KEY_MAP[GLFW_KEY_F18] = KEY_F18;
        KEY_MAP[GLFW_KEY_F19] = KEY_F19;
        KEY_MAP[GLFW_KEY_KP_0] = KEY_NUMPAD0;
        KEY_MAP[GLFW_KEY_KP_1] = KEY_NUMPAD1;
        KEY_MAP[GLFW_KEY_KP_2] = KEY_NUMPAD2;
        KEY_MAP[GLFW_KEY_KP_3] = KEY_NUMPAD3;
        KEY_MAP[GLFW_KEY_KP_4] = KEY_NUMPAD4;
        KEY_MAP[GLFW_KEY_KP_5] = KEY_NUMPAD5;
        KEY_MAP[GLFW_KEY_KP_6] = KEY_NUMPAD6;
        KEY_MAP[GLFW_KEY_KP_7] = KEY_NUMPAD7;
        KEY_MAP[GLFW_KEY_KP_8] = KEY_NUMPAD8;
        KEY_MAP[GLFW_KEY_KP_9] = KEY_NUMPAD9;
        KEY_MAP[GLFW_KEY_KP_DECIMAL] = KEY_DECIMAL;
        KEY_MAP[GLFW_KEY_KP_DIVIDE] = KEY_DIVIDE;
        KEY_MAP[GLFW_KEY_KP_MULTIPLY] = KEY_MULTIPLY;
        KEY_MAP[GLFW_KEY_KP_SUBTRACT] = KEY_SUBTRACT;
        KEY_MAP[GLFW_KEY_KP_ADD] = KEY_ADD;
        KEY_MAP[GLFW_KEY_KP_ENTER] = KEY_NUMPADENTER;
        KEY_MAP[GLFW_KEY_KP_EQUAL] = KEY_NUMPADEQUALS;
        KEY_MAP[GLFW_KEY_LEFT_SHIFT] = KEY_LSHIFT;
        KEY_MAP[GLFW_KEY_LEFT_CONTROL] = KEY_LCONTROL;
        KEY_MAP[GLFW_KEY_LEFT_ALT] = KEY_LMENU;
        KEY_MAP[GLFW_KEY_LEFT_SUPER] = KEY_LMETA;
        KEY_MAP[GLFW_KEY_RIGHT_SHIFT] = KEY_RSHIFT;
        KEY_MAP[GLFW_KEY_RIGHT_CONTROL] = KEY_RCONTROL;
        KEY_MAP[GLFW_KEY_RIGHT_ALT] = KEY_RMENU;
        KEY_MAP[GLFW_KEY_RIGHT_SUPER] = KEY_RMETA;
    }
}
