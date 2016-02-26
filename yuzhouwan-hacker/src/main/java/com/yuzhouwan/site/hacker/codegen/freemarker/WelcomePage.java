package com.yuzhouwan.site.hacker.codegen.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WelcomePage {

    public static void main(String[] args) throws Exception {

        /**
         * <html>
         * <head>
         *     <title>Welcome!</title>
         * </head>
         * <body>
         * <h1>Welcome asdf!</h1>
         * <p>Our latest product:
         *     <a href="http://www.yuzhouwan.com">asdf2014</a>!
         * </body>
         * </html>
         */
        WelcomePage welcomePage = new WelcomePage();
        welcomePage.codeGen();
    }

    public void codeGen() throws Exception {

        String baseDir = "F:\\yuzhouwan\\field\\src\\main\\resources\\freemarker\\";
        String ftlDir = baseDir.concat("ftl");
        Configuration cfg = config(ftlDir);

        Map<String, Object> root = dataModel();

        String ftlPath = "html.ftl";
        String outPath = baseDir.concat("\\out\\ftl.html");
        showUp(cfg, root, ftlPath, outPath);
    }

    /**
     * 获取模板 & 合并模板和数据模型
     *
     * @param cfg
     * @param root
     * @throws IOException
     * @throws TemplateException
     */
    private void showUp(Configuration cfg, Map<String, Object> root, String ftlPath, String outPath) throws IOException, TemplateException {
        Template temp = cfg.getTemplate(ftlPath);
        Writer out = new OutputStreamWriter(System.out);
        temp.process(root, out);

        FileWriter fileWriter = new FileWriter(new File(outPath));
        temp.process(root, fileWriter);
    }

    /**
     * 创建 Configuration 实例
     *
     * @return
     * @throws IOException
     */
    private Configuration config(String ftlDir) throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setDirectoryForTemplateLoading(new File(ftlDir));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg;
    }

    /**
     * 创建数据模型
     *
     * @return
     */
    private Map<String, Object> dataModel() {
        // Create the root hash
        Map<String, Object> root = new HashMap<>();
        // Put string ``user'' into the root
        root.put("user", "asdf");

        // Create the hash for ``latestProduct''
        Map<String, Object> blog = new HashMap<>();
        // and put it into the root
        root.put("blog", blog);

        // put ``url'' and ``name'' into latest
        blog.put("url", "http://www.yuzhouwan.com");
        blog.put("name", "Asdf's Blog");
        return root;
    }

}