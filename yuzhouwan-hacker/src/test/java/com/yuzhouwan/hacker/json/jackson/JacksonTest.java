package com.yuzhouwan.hacker.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Copyright @ 2024 yuzhouwan.com
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
            Assertions.assertEquals(0, bean.getId());
            Assertions.assertNull(bean.getName());
            Assertions.assertNull(bean.getBlog());
        }
        {
            final JacksonBean bean = new ObjectMapper()
                    .readerFor(JacksonBean.class)
                    .readValue("{\"id\":1}");
            Assertions.assertEquals(1, bean.getId());
            Assertions.assertNull(bean.getName());
            Assertions.assertNull(bean.getBlog());
        }
        {
            final JacksonBean bean = new ObjectMapper()
                    .readerFor(JacksonBean.class)
                    .readValue("{\"id\":2,\"name\":\"宇宙湾\",\"blog\":\"yuzhouwan.com\"}");
            Assertions.assertEquals(2, bean.getId());
            Assertions.assertEquals("宇宙湾", bean.getName());
            Assertions.assertEquals("yuzhouwan.com", bean.getBlog());
        }
        {
            final JacksonBean bean = new ObjectMapper()
                    .readerFor(JacksonBean.class)
                    .readValue("{\"id\":3,\"name\":\"asdf2014\",\"theBlog\":\"yuzhouwan.com\"}");
            Assertions.assertEquals(3, bean.getId());
            Assertions.assertEquals("asdf2014", bean.getName());
            Assertions.assertEquals("yuzhouwan.com", bean.getBlog());
        }
    }
}
