package com.yuzhouwan.common.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Benedict Jin on 2016/4/21.
 */
public class FileUtils {

    public static final String PREFIX = PropUtils.getInstance().getProperty("convert.input.stream.into.file.temp.path");
    public static final String SUFFIX = PropUtils.getInstance().getProperty("convert.input.stream.into.file.temp.file.type");

    public static File stream2file(InputStream in) throws IOException {

        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }
}
