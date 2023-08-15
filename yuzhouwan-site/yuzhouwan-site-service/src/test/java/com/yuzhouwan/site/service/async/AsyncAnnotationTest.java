package com.yuzhouwan.site.service.async;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Async Annotation Tester
 *
 * @author Benedict Jin
 * @since 2016/8/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "site/src/main/resources")
@ContextConfiguration(locations = {
        "classpath:/service/spring.service.xml" /*,*/
})
public class AsyncAnnotationTest {

    private static final long TTL = 5;

    @Autowired
    private AsyncAnnotation asyncAnnotation;

    @Test
    public void longTimeOperationTest() throws Exception {
        long begin, end;
        {
            begin = System.currentTimeMillis();
            asyncAnnotation.longTimeOperation(TTL);
            end = System.currentTimeMillis();
            System.out.printf("Normal Async Done in %d milliseconds%n", end - begin);
        }
        {
            begin = System.currentTimeMillis();
            asyncAnnotation.longTimeOperationInternal(TTL);
            end = System.currentTimeMillis();
            System.out.printf("Internal Async Done in %d milliseconds%n", end - begin);
        }
        {
            begin = System.currentTimeMillis();
            asyncAnnotation.longTimeOperationOrigin(TTL);
            end = System.currentTimeMillis();
            System.out.printf("Origin Async Done in %d milliseconds%n", end - begin);
        }
    }
}
