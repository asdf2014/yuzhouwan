package com.yuzhouwan.common.util;

import java.io.File;

/**
 * Created by Benedict Jin on 2016/4/7.
 */
public class DirUtils {

    public static void createOutDir() {

        String currentDir = System.getProperty("user.dir");
        String outDirPath = currentDir.concat("\\out");
        File outDir = new File(outDirPath);
        if (!outDir.exists()) {
            outDir.mkdir();
        }
        System.out.println(currentDir);
    }
}
