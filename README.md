# LegacyLWJGLAgent

An **extremely** over-engineered java agent to replace LWJGL2 with LWJGL3.

Supports (probably) any Minecraft version using LWJGL2, and Java 8. (Tested with 1.12.2)

This was written in like 2 days, and is a work in progress. However, the game does
boot, with a few remaining problems.

### Usage
Append `-javaagent:/path/to/the/agent.jar` to your JVM arguments.

I wouldn't consider this production ready yet, there is still some issues.

### How??
The java agent first yeets LWJGL2 from the classpath (~~oh god the reflection!~~),
and appends LWJGL3.

The agent provides a framework for extending existing LWJGL3 classes with extra
implementation, called Shims. These shims re-implement removed LWJGL functions from
the 2 -> 3 transition. In the case of OpenGL, these are mostly just renames, or removal of
older helpers for specific GL calls. In the case of OpenAL, mostly the same, however, context
creation is entirely different.

Classes which are completely missing are either, copied from LWJGL2, 
re-implemented or, are stubbed out.

### Why??
Well, as all good projects, the problem stems from X11 imploding (Thanks NVIDIA!) whenever I started an older
Minecraft version (compositor lag, yay!). So this was the
_totally obvious and sane solution_.

### Credits
- The LWJGL developers. Large portions of the code here are copied directly.
  - Any copied file retains original copyright headers, and is licensed the same.
  - Modifications include stubbing methods, re-implementing, and formatting.
- https://github.com/Zarzelcow/legacy-lwjgl3 for some overall guidance.
  - https://github.com/gudenau/MC-LWJGL3 transitively from legacy-lwjgl3.

### License
- All code provided in the `org/lwjgl/*` package is licensed under the LWJGL2 BSD-3 clause license.
- All other code is provided under the MIT license.

Please see the associated License files.

### Contributing
Contributions are welcome, please come chat with me on [Discord](https://discord.gg/9nr3qyC), or submit
an issue, so we can coordinate.

### TODO:
- Implement Forge/Fabric Mod variants.
- MORE SHIMS!
- Java++ support?
- Un-scuff this readme.
