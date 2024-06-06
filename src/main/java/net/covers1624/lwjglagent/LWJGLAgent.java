package net.covers1624.lwjglagent;

import net.covers1624.quack.collection.ColUtils;
import net.covers1624.quack.collection.FastStream;
import net.covers1624.quack.io.IOUtils;
import net.covers1624.quack.util.SneakyUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.URLClassPath;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by covers1624 on 2/6/24.
 */
public class LWJGLAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(LWJGLAgent.class);

    private static final Path DATA_DIRECTORY = Paths.get("./.lwjglagent");
    private static final List<Path> LWJGL3_JARS = new ArrayList<>();

    private static final String JAR_EMBED_PREFIX = "META-INF/jars/";

    private static final List<Pattern> LWJGL2_JARS = Arrays.asList(
            Pattern.compile("/jinput-.+\\.jar"),
            Pattern.compile("/jutils-.+\\.jar"),
            Pattern.compile("/lwjgl-2.+\\.jar")
    );

    public static boolean DEBUG = false;

    public static void premain(@Nullable String args, Instrumentation inst) throws IOException {
        if (args != null && args.contains("debug")) {
            DEBUG = true;
        }
        LOGGER.info("Initializing LWJGLAgent.");
        Path jar = getOurPath();
        LOGGER.info("Found jar at: {}", jar);
        setupDataDir(jar);

        try {
            spliceClassLoader();
        } catch (Throwable ex) {
            throw new RuntimeException("Failed to splice system classloader.", ex);
        }

        Path shimJar = FastStream.of(LWJGL3_JARS)
                .filter(e -> e.getFileName().toString().equals("lwjgl-shim.jar"))
                .only();
        inst.addTransformer(new ClassShimTransformer(shimJar));
    }

    private static void spliceClassLoader() throws Throwable {
        LOGGER.info("Splicing System ClassLoader to remove LWJGL2 and insert LWJGL3");
        Class<URLClassLoader> c_URLClassLoader = URLClassLoader.class;
        Field f_ucp = c_URLClassLoader.getDeclaredField("ucp");
        Method m_addURL = c_URLClassLoader.getDeclaredMethod("addURL", URL.class);
        m_addURL.setAccessible(true);

        Class<URLClassPath> c_URLClassPath = URLClassPath.class;
        Field f_urls = c_URLClassPath.getDeclaredField("urls");
        Field f_path = c_URLClassPath.getDeclaredField("path");
        Field f_loaders = c_URLClassPath.getDeclaredField("loaders");
        Field f_lmap = c_URLClassPath.getDeclaredField("lmap");

        Class<?> c_URLCP_JarLoader = Class.forName("sun.misc.URLClassPath$JarLoader");
        Field f_csu = c_URLCP_JarLoader.getDeclaredField("csu");

        URLClassLoader classLoader = ((URLClassLoader) ClassLoader.getSystemClassLoader());
        URLClassPath classPath = privateGet(f_ucp, classLoader);
        Stack<URL> urls = privateGet(f_urls, classPath);
        ArrayList<URL> path = privateGet(f_path, classPath);
        ArrayList<Object> loaders = privateGet(f_loaders, classPath);
        Map<String, Object> lmap = privateGet(f_lmap, classPath);

        List<URL> toRemove = FastStream.of(path)
                .filter(e -> isStrippedJar(e.toString()))
                .toList();
        LOGGER.info("Identified {} jars to remove from System ClassLoader:", toRemove.size());
        for (URL url : toRemove) {
            LOGGER.info("  {}", url);
        }

        LOGGER.info("Beginning splicing...");
        synchronized (urls) {
            urls.removeAll(toRemove);
        }

        for (Iterator<Object> iterator = loaders.iterator(); iterator.hasNext(); ) {
            Object loader = iterator.next();
            if (!c_URLCP_JarLoader.isAssignableFrom(loader.getClass())) continue;
            URL csu = privateGet(f_csu, loader);
            if (toRemove.contains(csu)) {
                LOGGER.info(" Identified loader for {}: {}", csu, loader);
                lmap.entrySet().removeIf(e -> e.getValue() == loader);
                ((Closeable) loader).close();
                iterator.remove();
            }
        }
        LOGGER.info("Removed LWJGL2 from classpath.");

        LOGGER.info("Adding LWJGL3 to classpath..");
        for (Path jar : LWJGL3_JARS) {
            m_addURL.invoke(classLoader, jar.toUri().toURL());
        }
        LOGGER.info("Splicing complete!");
    }

    public static boolean isStrippedJar(String path) {
        return ColUtils.anyMatch(LWJGL2_JARS, p -> p.matcher(path).find());
    }

    private static void setupDataDir(Path ourJar) throws IOException {
        LOGGER.info("Setting up data directory at {}", DATA_DIRECTORY);

        extractLWJGL(ourJar);
    }

    private static void extractLWJGL(Path ourJar) throws IOException {
        LOGGER.info("Extracting jars..");
        try (ZipFile zip = new ZipFile(ourJar.toFile())) {
            for (ZipEntry entry : ColUtils.iterable(zip.entries())) {
                if (entry.isDirectory()) continue;
                String eName = entry.getName();
                if (eName.startsWith(JAR_EMBED_PREFIX)) {
                    Path extract = DATA_DIRECTORY.resolve(eName.substring(JAR_EMBED_PREFIX.length()));
                    LOGGER.info("Extracting {} to {}", eName, extract);
                    try (InputStream is = zip.getInputStream(entry)) {
                        Files.copy(is, IOUtils.makeParents(extract), StandardCopyOption.REPLACE_EXISTING);
                    }
                    LWJGL3_JARS.add(extract);
                }
            }
        }
    }

    private static Path getOurPath() {
        try {
            ProtectionDomain domain = LWJGLAgent.class.getProtectionDomain();
            CodeSource source = domain.getCodeSource();
            URL location = source.getLocation();
            return Paths.get(location.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteFolder(Path folder) throws IOException {
        try (Stream<Path> stream = Files.walk(folder)) {
            stream.sorted(Comparator.reverseOrder()).forEach(SneakyUtils.sneak(Files::delete));
        }
    }

    private static <T> T privateGet(Field field, Object instance) throws IllegalAccessException {
        field.setAccessible(true);
        return SneakyUtils.unsafeCast(field.get(instance));
    }
}
