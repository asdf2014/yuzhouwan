package effective.encoding;

import java.nio.charset.Charset;

/**
 * Copyright @ yuzhouwan.com
 * All right reserved.
 * Function：effective.encoding
 *
 * @author asdf2014
 * @since 2015/11/19
 */
public class ConvertEncoding {

    public void showUtf8InGBK(String utf8){
        System.out.println(new String(utf8.getBytes(), Charset.forName("GBK")));
    }

    public void showUtf8InGB2312(String utf8){
        System.out.println(new String(utf8.getBytes(), Charset.forName("GB2312")));
    }

}
