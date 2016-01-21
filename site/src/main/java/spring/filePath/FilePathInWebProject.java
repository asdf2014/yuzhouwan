package spring.filePath;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：make sure about file path
 *
 * @author jinjy
 * @since 2015/12/31 0031
 */
@Component
public class FilePathInWebProject {

    @Value("#{appProperties.date}")
    private String date;

    public void showDate() throws IOException {
        InputStream is = getClass().getResourceAsStream("/configuration/properties.xml");
        Properties p = new Properties();
        p.load(is);

        for (Object k : p.keySet()) {
            String key = k + "";
            String value = p.getProperty(key);
            System.out.println(key.concat(" - ").concat(value));
        }
        System.out.println(date);
    }
}
