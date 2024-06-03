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

import net.covers1624.lwjglagent.StubbedMethod;
import org.lwjgl.LWJGLException;

/**
 * <p/>
 * Manages GL contexts. Before any rendering is done by a LWJGL system, a call should be made to GLContext.useContext() with a
 * context. This will ensure that GLContext has an accurate reflection of the current context's capabilities and function
 * pointers.
 * <p/>
 * This class is thread-safe in the sense that multiple threads can safely call all public methods. The class is also
 * thread-aware in the sense that it tracks a per-thread current context (including capabilities and function pointers).
 * That way, multiple threads can have multiple contexts current and render to them concurrently.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$
 * $Id$
 */
public class GLContext {

    public static ContextCapabilities getCapabilities() {
        return new ContextCapabilities();
    }

    public static synchronized void useContext(Object context) throws LWJGLException {
        throw new StubbedMethod();
    }

    public static synchronized void useContext(Object context, boolean forwardCompatible) throws LWJGLException {
        throw new StubbedMethod();
    }

    public static synchronized void loadOpenGLLibrary() throws LWJGLException {
        throw new StubbedMethod();
    }

    public static synchronized void unloadOpenGLLibrary() {
        throw new StubbedMethod();
    }
}
