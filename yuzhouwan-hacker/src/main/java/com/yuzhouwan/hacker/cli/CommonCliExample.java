package com.yuzhouwan.hacker.cli;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Common Cli Example
 *
 * @author Benedict Jin
 * @since 2017/7/6
 */
public class CommonCliExample {

    private static final Logger _log = LoggerFactory.getLogger(CommonCliExample.class);

    public static void main(String[] args) {
        String[] help = {"-h"};
        String[] blogInfos = {"-b", "yuzhouwan", "-s", "yuzhouwan.com", "-t", "BigData"};

        parseParams(help);
        parseParams(blogInfos);
    }

    private static void parseParams(String[] args) {
        Options opts = new Options();
        opts.addOption("h", false, "Help");
        opts.addOption("b", true, "Blog");
        opts.addOption("s", true, "Site");
        opts.addOption("t", true, "Topic");
        CommandLineParser parser = new DefaultParser();
        CommandLine cli;
        try {
            cli = parser.parse(opts, args);
            if (cli.getOptions().length > 0) {
                if (cli.hasOption('h')) {
                    HelpFormatter hf = new HelpFormatter();
                    hf.printHelp("All Options", opts);
                } else {
                    System.out.println(cli.getOptionValue("b"));
                    System.out.println(cli.getOptionValue("s"));
                    System.out.println(cli.getOptionValue("t"));
                }
            } else {
                HelpFormatter hf = new HelpFormatter();
                hf.printHelp("May Options", opts);
            }
        } catch (ParseException e) {
            _log.error("{}", e);
        }
    }
}
