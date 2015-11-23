package effective.encoding;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Copyright @ yuzhouwan.com
 * All right reserved.
 * Function：ConvertEncoding Tester
 *
 * @author jinjy
 * @since 2015/11/19
 */
public class ConvertEncodingTest {

    private static final String utf8 = new String("阿斯多夫".getBytes(), Charset.forName("UTF-8"));
    private ConvertEncoding convertEncoding;

    @Before
    public void before() throws Exception {
        convertEncoding = new ConvertEncoding();
    }

    @Before
    public void after() throws Exception {

    }

    /**
     * Method: showUtf8InGB2312(String utf8)
     */
    @Test
    public void testShowUtf8InGB2312() throws Exception {

        convertEncoding.showUtf8InGB2312(utf8);
    }

    /**
     * Method: showUtf8InGBK(String utf8)
     */
    @Test
    public void testShowUtf8InGBK() throws Exception {

        convertEncoding.showUtf8InGBK(utf8);
    }

} 
