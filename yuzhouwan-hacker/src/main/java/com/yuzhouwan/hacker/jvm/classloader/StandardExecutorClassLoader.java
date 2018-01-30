package com.yuzhouwan.hacker.jvm.classloader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Standard Executor ClassLoader
 *
 * @author Benedict Jin
 * @since 2018/1/26
 */
public class StandardExecutorClassLoader extends URLClassLoader {

    private static final String JAR_POSTFIX = ".jar";
    private static final String URL_PROTOCOL_FILE = "file";
    private static final String BASE_PATH = System.getProperty("user.dir");
    private static final String EXTENSION_PATH = BASE_PATH.concat(File.separator).concat("ext").concat(File.separator);

    public StandardExecutorClassLoader(String version) {
        super(new URL[]{}, null);   // set parent classLoader as null.
        loadResource(version);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        System.out.println("Loading class: " + name);
        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            return StandardExecutorClassLoader.class.getClassLoader().loadClass(name);
        }
    }

    private void loadResource(String version) {
        String jarPath = EXTENSION_PATH + version;
        loadJar(jarPath);
        loadJar(jarPath.concat(File.separator).concat("lib"));
    }

    private void loadJar(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) return;
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file == null || !file.isFile() || !file.getName().endsWith(JAR_POSTFIX)) continue;
            this.addURL(file);
        }
    }

    private void addURL(File file) {
        try {
            super.addURL(new URL(URL_PROTOCOL_FILE, null, file.getCanonicalPath()));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Cannot add url [%s]!", file.getAbsolutePath()), e);
        }
    }
}
