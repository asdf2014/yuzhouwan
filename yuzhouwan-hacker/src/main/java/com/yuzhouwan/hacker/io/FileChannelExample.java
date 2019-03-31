package com.yuzhouwan.hacker.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.yuzhouwan.common.dir.DirUtils.PROJECT_BASE_PATH;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：File Channel Example
 *
 * @author Benedict Jin
 * @since 2017/6/28
 */
public class FileChannelExample {

    public static void main(String[] args) throws IOException {
        String path = PROJECT_BASE_PATH.concat("\\yuzhouwan-hacker\\src\\main\\resources\\logback.xml");
        System.out.println(path);
        File file = new File(path);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("yuzhouwan.com".getBytes());
            fos.flush();
            System.out.println(fos.getChannel().size());
        }
    }
}
