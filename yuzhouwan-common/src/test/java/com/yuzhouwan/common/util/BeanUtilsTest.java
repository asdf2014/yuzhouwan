package com.yuzhouwan.common.util;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：Bean Utils Test
 *
 * @author Benedict Jin
 * @since 2016/12/1
 */
public class BeanUtilsTest {

    @Test
    public void testSwapper() {

        BeanA beanA = new BeanA();
        BeanUtils.swapper(beanA, "aA", 1, "");
        BeanUtils.swapper(beanA, "bB", 2L, "_");
        BeanUtils.swapper(beanA, "c_C", 3d, "_");
        BeanUtils.swapper(beanA, "dD", beanA, "_");

        assertEquals("{\"aA\":1,\"bB\":2,\"cC\":3,\"d_D\":{\"$ref\":\"@\"}}", beanA.toString());
    }

    private class BeanA {
        private int aA;
        public long bB;
        private Double cC;
        private Object d_D;

        public int getaA() {
            return aA;
        }

        public Double getcC() {
            return cC;
        }

        public Object getD_D() {
            return d_D;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }
}
