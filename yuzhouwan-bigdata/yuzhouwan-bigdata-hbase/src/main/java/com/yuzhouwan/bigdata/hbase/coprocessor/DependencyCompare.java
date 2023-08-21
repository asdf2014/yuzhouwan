package com.yuzhouwan.bigdata.hbase.coprocessor;

import com.yuzhouwan.common.dir.DirUtils;
import com.yuzhouwan.common.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：DependencyCompare
 *
 * @author Benedict Jin
 * @since 2017/4/25
 */
public class DependencyCompare {

    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyCompare.class);
    private static final String HBASE_DEPENDENCIES = DirUtils.PROJECT_BASE_PATH.concat(
            "\\yuzhouwan-bigdata\\yuzhouwan-bigdata-hbase\\src\\main\\resources\\coprocessor\\"
                    + "hbase-0.98.8-hadoop2.dependency");

    public static void main(String[] args) throws Exception {
        if (args == null || args.length != 1) {
            LOGGER.error(String.format("Params [%s] wrong!", Arrays.toString(args)));
            LOGGER.info("Usage: jar -jar com.yuzhouwan.bigdata.hbase.coprocessor.DependencyCompare "
                    + "<Coprocessor's dependency path>");
            System.exit(-1);
        }
        String path = args[0];
        File coprocessorFile;
        if (!FileUtils.checkExist(coprocessorFile = new File(path))) {
            LOGGER.error(String.format("Path [%s] not exist!", path));
            System.exit(-2);
        }
        String[] coprocessorDependencies = coprocessorFile.list();
        if (coprocessorDependencies == null || coprocessorDependencies.length == 0) {
            LOGGER.info("Empty directory.");
            System.exit(0);
        }
        byte[] hbaseDependenciesBytes = FileUtils.readFile(HBASE_DEPENDENCIES);
        if (hbaseDependenciesBytes == null || hbaseDependenciesBytes.length == 0) {
            LOGGER.error(String.format("Cannot get those dependencies from %s!", HBASE_DEPENDENCIES));
            System.exit(-3);
        }
        String[] hbaseDependenciesStrs = new String(hbaseDependenciesBytes).split("\n");
        /*
         TODO{Benedict Jin}: compare those list of jars
         (So far, using `mvn versions:compare-dependencies` command is good to me)
         */
    }
}
