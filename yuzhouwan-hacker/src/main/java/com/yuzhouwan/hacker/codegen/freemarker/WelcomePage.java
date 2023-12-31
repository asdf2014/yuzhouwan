package com.yuzhouwan.hacker.codegen.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：动态地将数据，填充到 freemarker模板中
 *
 * @author Benedict Jin
 * @since 2016/1/26 0026
 */
public class WelcomePage {

    private static final String BASE_DIR = System.getProperty("user.dir")
            .concat("\\yuzhouwan-hacker\\src\\main\\resources\\freemarker\\");

    private static final String FTL_DIR = BASE_DIR.concat("ftl");
    private static final String OUT_PATH = BASE_DIR.concat("\\outdir\\ftl.html");

    private static final String FTL_PATH = "html.ftl";

    public static void main(String[] args) throws Exception {

        /*
         * <html>
         * <head>
         *     <title>Welcome!</title>
         * </head>
         * <body>
         * <h1>Welcome asdf!</h1>
         * <p>Our latest product:
         *     <a href="https://yuzhouwan.com">asdf2014</a>!
         * </body>
         * </html>
         */
        WelcomePage welcomePage = new WelcomePage();
        welcomePage.codeGen();
    }

    public void codeGen() throws Exception {

        Configuration cfg = config(FTL_DIR);
        Map<String, Object> root = dataModel();
        showUp(cfg, root, FTL_PATH, OUT_PATH);
    }

    /**
     * 获取模板 & 合并模板和数据模型.
     *
     * @param cfg
     * @param root
     * @throws IOException
     * @throws TemplateException
     */
    private void showUp(Configuration cfg, Map<String, Object> root, String ftlPath, String outPath)
            throws IOException, TemplateException {
        Template temp = cfg.getTemplate(ftlPath);
        Writer out = new OutputStreamWriter(System.out);
        temp.process(root, out);

        try (FileWriter fileWriter = new FileWriter(new File(outPath))) {
            temp.process(root, fileWriter);
        }
    }

    /**
     * 创建 Configuration 实例.
     *
     * @return
     * @throws IOException
     */
    private Configuration config(String ftlDir) throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setDirectoryForTemplateLoading(new File(ftlDir));
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg;
    }

    /**
     * 创建数据模型.
     */
    private Map<String, Object> dataModel() {
        // Create the root hash
        Map<String, Object> root = new HashMap<>();
        // Put string ``user'' into the root
        root.put("user", "asdf");

        // Create the hash for ``latestProduct''
        Map<String, Object> blog = new HashMap<>();

        // put ``url'' and ``name'' into latest
        blog.put("url", "https://yuzhouwan.com");
        blog.put("name", "Asdf's Blog");

        // and put it into the root
        root.put("blog", blog);

        return root;
    }
}
