package com.yuzhouwan.hacker.jvm.classloader;

import java.io.File;
import java.io.IOException;
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

    private static final String BASE_DIR = System.getProperty("user.dir") + File.separator + "ext" + File.separator;

    public StandardExecutorClassLoader(String version) {
        // 将 Parent 设置为 null.
        super(new URL[]{}, null);
        loadResource(version);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // 测试时可打印看一下.
        System.out.println("Class loader: " + name);
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
        String jarPath = BASE_DIR + version;

        // 加载对应版本目录下的 Jar 包.
        tryLoadJarInDir(jarPath);
        // 加载对应版本目录下的 lib 目录下的 Jar 包.
        tryLoadJarInDir(jarPath + File.separator + "lib");
    }

    private void tryLoadJarInDir(String dirPath) {
        File dir = new File(dirPath);
        // 自动加载目录下的 Jar 包.
        if (dir.exists() && dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    this.addURL(file);
                }
            }
        }
    }

    private void addURL(File file) {
        try {
            super.addURL(new URL("file", null, file.getCanonicalPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
