package org.lwjgl.opengl.shim;

import net.covers1624.lwjglagent.StubbedMethod;
import net.covers1624.lwjglagent.shim.Named;
import net.covers1624.lwjglagent.shim.Shim;
import org.lwjgl.PointerBuffer;

import java.nio.*;

import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memByteBuffer;

@Shim
public class GL45 extends org.lwjgl.opengl.GL45 {

    public static void glGetTransformFeedback(int xfb, int pname, IntBuffer param) {
        glGetTransformFeedbackiv(xfb, pname, param);
    }

    public static void glGetTransformFeedback(int xfb, int pname, int index, IntBuffer param) {
        glGetTransformFeedbacki_v(xfb, pname, index, param);
    }

    public static void glGetTransformFeedback(int xfb, int pname, int index, LongBuffer param) {
        glGetTransformFeedbacki64_v(xfb, pname, index, param);
    }

    public static void glNamedBufferStorage(int buffer, LongBuffer data, int flags) {
        nglNamedBufferStorage(buffer, data.remaining(), memAddress(data), flags);
    }

    public static void glGetNamedBufferParameter(int buffer, int pname, IntBuffer params) {
        glGetNamedBufferParameteriv(buffer, pname, params);
    }

    public static void glGetNamedBufferParameter(int buffer, int pname, LongBuffer params) {
        glGetNamedBufferParameteri64v(buffer, pname, params);
    }

    @Named ("glGetNamedBufferPointer")
    public static ByteBuffer glGetNamedBufferPointer2(int buffer, int pname) {
        return memByteBuffer(
                glGetNamedBufferPointer(buffer, pname),
                glGetNamedBufferParameteri(buffer, GL_BUFFER_SIZE)
        );
    }

    public static void glClearNamedFramebuffer(int framebuffer, int buffer, int drawbuffer, IntBuffer value) {
        glClearNamedFramebufferiv(framebuffer, buffer, drawbuffer, value);
    }

    public static void glClearNamedFramebufferu(int framebuffer, int buffer, int drawbuffer, IntBuffer value) {
        glClearNamedFramebufferuiv(framebuffer, buffer, drawbuffer, value);
    }

    public static void glClearNamedFramebuffer(int framebuffer, int buffer, int drawbuffer, FloatBuffer value) {
        glClearNamedFramebufferfv(framebuffer, buffer, drawbuffer, value);
    }

    public static void glClearNamedFramebufferfi(int framebuffer, int buffer, float depth, int stencil) {
        // This GL call doesn't actually exist. It's not documented.
        // https://registry.khronos.org/OpenGL-Refpages/gl4/html/glClearBuffer.xhtml
        throw new StubbedMethod();
    }

    public static void glGetNamedFramebufferParameter(int framebuffer, int pname, IntBuffer params) {
        glGetNamedFramebufferParameteriv(framebuffer, pname, params);
    }

    public static int glGetNamedFramebufferParameter(int framebuffer, int pname) {
        return glGetNamedFramebufferParameteri(framebuffer, pname);
    }

    public static void glGetNamedFramebufferAttachmentParameter(int framebuffer, int attachment, int pname, IntBuffer params) {
        glGetNamedFramebufferAttachmentParameteriv(framebuffer, attachment, pname, params);
    }

    public static int glGetNamedFramebufferAttachmentParameter(int framebuffer, int attachment, int pname) {
        return glGetNamedFramebufferAttachmentParameteri(framebuffer, attachment, pname);
    }

    public static void glGetNamedRenderbufferParameter(int renderbuffer, int pname, IntBuffer params) {
        glGetNamedRenderbufferParameteriv(renderbuffer, pname, params);
    }

    public static int glGetNamedRenderbufferParameter(int renderbuffer, int pname) {
        return glGetNamedRenderbufferParameteri(renderbuffer, pname);
    }

    public static void glCompressedTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, ByteBuffer data) {
        glCompressedTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, memAddress(data));
    }

    public static void glTextureParameter(int texture, int pname, FloatBuffer params) {
        glTextureParameterfv(texture, pname, params);
    }

    public static void glTextureParameterI(int texture, int pname, IntBuffer params) {
        glTextureParameterIiv(texture, pname, params);
    }

    public static void glTextureParameterIu(int texture, int pname, IntBuffer params) {
        glTextureParameterIuiv(texture, pname, params);
    }

    public static void glTextureParameter(int texture, int pname, IntBuffer params) {
        glTextureParameteriv(texture, pname, params);
    }

    public static void glGetCompressedTextureImage(int texture, int level, IntBuffer pixels) {
        glGetCompressedTextureImage(texture, level, memByteBuffer(pixels));
    }

    public static void glGetCompressedTextureImage(int texture, int level, ShortBuffer pixels) {
        glGetCompressedTextureImage(texture, level, memByteBuffer(pixels));
    }

    public static void glGetTextureLevelParameter(int texture, int level, int pname, FloatBuffer params) {
        glGetTextureLevelParameterfv(texture, level, pname, params);
    }

    public static void glGetTextureLevelParameter(int texture, int level, int pname, IntBuffer params) {
        glGetTextureLevelParameteriv(texture, level, pname, params);
    }

    public static void glGetTextureParameter(int texture, int pname, FloatBuffer params) {
        glGetTextureParameterfv(texture, pname, params);
    }

    public static void glGetTextureParameterI(int texture, int pname, IntBuffer params) {
        glGetTextureParameterIiv(texture, pname, params);
    }

    public static void glGetTextureParameterIu(int texture, int pname, IntBuffer params) {
        glGetTextureParameterIuiv(texture, pname, params);
    }

    public static void glGetTextureParameter(int texture, int pname, IntBuffer params) {
        glGetTextureParameteriv(texture, pname, params);
    }

    public static void glVertexArrayVertexBuffers(int vaobj, int first, int count, IntBuffer buffers, PointerBuffer offsets, IntBuffer strides) {
        nglVertexArrayVertexBuffers(vaobj, first, count, memAddress(buffers), memAddress(offsets), memAddress(strides));
    }

    public static void glGetVertexArray(int vaobj, int pname, IntBuffer param) {
        glGetVertexArrayiv(vaobj, pname, param);
    }

    public static int glGetVertexArray(int vaobj, int pname) {
        return glGetVertexArrayi(vaobj, pname);
    }

    public static void glGetVertexArrayIndexed(int vaobj, int index, int pname, IntBuffer param) {
        glGetVertexArrayIndexediv(vaobj, index, pname, param);
    }

    public static int glGetVertexArrayIndexed(int vaobj, int index, int pname) {
        return glGetVertexArrayIndexedi(vaobj, index, pname);
    }

    public static void glGetVertexArrayIndexed64i(int vaobj, int index, int pname, LongBuffer param) {
        glGetVertexArrayIndexed64iv(vaobj, index, pname, param);
    }

    public static void glReadnPixels(int x, int y, int width, int height, int format, int type, DoubleBuffer pixels) {
        nglReadnPixels(x, y, width, height, format, type, pixels.remaining(), memAddress(pixels));
    }

    public static void glGetnUniform(int program, int location, FloatBuffer params) {
        glGetnUniformfv(program, location, params);
    }

    public static void glGetnUniform(int program, int location, IntBuffer params) {
        glGetnUniformiv(program, location, params);
    }

    public static void glGetnUniformu(int program, int location, IntBuffer params) {
        glGetnUniformuiv(program, location, params);
    }
}
