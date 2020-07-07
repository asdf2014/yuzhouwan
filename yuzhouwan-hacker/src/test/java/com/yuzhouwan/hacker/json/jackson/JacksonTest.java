package com.yuzhouwan.hacker.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：Jackson Test
 *
 * @author Benedict Jin
 * @since 2020/7/5
 */
public class JacksonTest {

    @Test
    public void test() throws Exception {
        {
            final JacksonBean bean = new ObjectMapper()
                    .readerFor(JacksonBean.class)
                    .readValue("{}");
            Assert.assertEquals(0, bean.getId());
            Assert.assertNull(bean.getName());
            Assert.assertNull(bean.getBlog());
        }
        {
            final JacksonBean bean = new ObjectMapper()
                    .readerFor(JacksonBean.class)
                    .readValue("{\"id\":1}");
            Assert.assertEquals(1, bean.getId());
            Assert.assertNull(bean.getName());
            Assert.assertNull(bean.getBlog());
        }
        {
            final JacksonBean bean = new ObjectMapper()
                    .readerFor(JacksonBean.class)
                    .readValue("{\"id\":2,\"name\":\"宇宙湾\",\"blog\":\"yuzhouwan.com\"}");
            Assert.assertEquals(2, bean.getId());
            Assert.assertEquals("宇宙湾", bean.getName());
            Assert.assertEquals("yuzhouwan.com", bean.getBlog());
        }
        {
            final JacksonBean bean = new ObjectMapper()
                    .readerFor(JacksonBean.class)
                    .readValue("{\"id\":3,\"name\":\"asdf2014\",\"theBlog\":\"yuzhouwan.com\"}");
            Assert.assertEquals(3, bean.getId());
            Assert.assertEquals("asdf2014", bean.getName());
            Assert.assertEquals("yuzhouwan.com", bean.getBlog());
        }
    }
}
