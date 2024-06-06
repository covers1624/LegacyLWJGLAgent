package org.lwjgl.openal.shim;

import net.covers1624.lwjglagent.shim.Rewire;
import net.covers1624.lwjglagent.shim.Shim;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.openal.ALC10.*;

@Shim ("org.lwjgl.openal.AL")
public class AL {

    private static long device = MemoryUtil.NULL;
    private static long context = MemoryUtil.NULL;

    @Rewire
    public static boolean isCreated() {
        return device != MemoryUtil.NULL;
    }

    @Rewire
    public static void create() throws LWJGLException {
        create(null, 44100, 60, false);
    }

    @Rewire
    public static void create(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized) throws LWJGLException {
        create(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, true);
    }

    @Rewire
    public static void create(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized, boolean openDevice) throws LWJGLException {
        if (isCreated()) {
            throw new IllegalStateException("Only one OpenAL context may be instantiated at any one time.");
        }
        device = ALC10.alcOpenDevice(deviceArguments);
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);

        if (contextFrequency == -1) {
            IntBuffer attribs = BufferUtils.createIntBuffer(7);
            attribs.put(ALC_FREQUENCY);
            attribs.put(contextFrequency);
            attribs.put(ALC_REFRESH);
            attribs.put(contextRefresh);
            attribs.put(ALC_SYNC);
            attribs.put(contextSynchronized ? ALC_TRUE : ALC_FALSE);
            attribs.put(0);
            context = ALC10.alcCreateContext(device, attribs);
        } else {
            context = ALC10.alcCreateContext(device, (IntBuffer) null);
        }

        if (!ALC10.alcMakeContextCurrent(context)) {
            throw new LWJGLException("Failed to make context current.");
        }

        org.lwjgl.openal.AL.createCapabilities(deviceCaps);
    }

//
//    public static ALCcontext getContext() {
//        throw new StubbedMethod();
//    }
//
//    public static ALCdevice getDevice() {
//        throw new StubbedMethod();
//    }
}
