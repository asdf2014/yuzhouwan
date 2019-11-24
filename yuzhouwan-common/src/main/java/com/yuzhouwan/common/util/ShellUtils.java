package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Shell Utils
 *
 * @author Benedict Jin
 * @since 2019/11/23
 */
public class ShellUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUtils.class);

    @SuppressWarnings("WeakerAccess")
    public static void callShellScript(String script) {
        try {
            String cmd = "/bin/sh -c " + script;
            String[] envs = {"val=2", "call=Bash Shell"};
            final Process process = Runtime.getRuntime().exec(cmd, envs);  // lgtm [java/concatenated-command-line]
            final InputStream outStream = process.getInputStream();
            shellOutput(outStream);
            final InputStream errorStream = process.getErrorStream();
            shellOutput(errorStream);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    private static void shellOutput(InputStream stream) throws IOException {
        try {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } finally {
            stream.close();
        }
    }

    public static void main(String[] args) {
        callShellScript(System.getProperty("user.dir") + "/yuzhouwan-common/src/main/resources/shell/max_cache.sh");
    }
}
