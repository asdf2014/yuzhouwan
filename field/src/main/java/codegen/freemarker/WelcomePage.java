package codegen.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
        Configuration cfg = config();
        Map<String, Object> root = dataModel();
        showUp(cfg, root);
    }

    /**
     * 获取模板 & 合并模板和数据模型
     *
     * @param cfg
     * @param root
     * @throws IOException
     * @throws TemplateException
     */
    private void showUp(Configuration cfg, Map<String, Object> root) throws IOException, TemplateException {
        Template temp = cfg.getTemplate("html.ftl");
        Writer out = new OutputStreamWriter(System.out);
        temp.process(root, out);
    }

    /**
     * 创建 Configuration 实例
     *
     * @return
     * @throws IOException
     */
    private Configuration config() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setDirectoryForTemplateLoading(new File("F:\\yuzhouwan\\field\\src\\main\\resources\\freemarker\\ftl"));
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
        Map<String, Object> latest = new HashMap<>();
        // and put it into the root
        root.put("latestProduct", latest);
        // put ``url'' and ``name'' into latest
        latest.put("url", "http://www.yuzhouwan.com");
        latest.put("name", "asdf2014");
        return root;
    }

}