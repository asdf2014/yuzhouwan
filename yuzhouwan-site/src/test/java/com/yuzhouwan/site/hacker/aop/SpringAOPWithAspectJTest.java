package com.yuzhouwan.site.hacker.aop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * SpringAOPWithAspectJ Tester.
 *
 * @author asdf2014
 * @version 1.0
 * @since 十一月 9, 2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/service/spring.service.xml")
public class SpringAOPWithAspectJTest extends AbstractJUnit4SpringContextTests {

    @Resource
    private TargetAOP targetAOP;

    @Test
    public void test() {

        targetAOP.targetBefore();
        targetAOP.targetAfter();
        targetAOP.targetAfterReturning(true);
        targetAOP.targetAfterReturning(false);
        System.out.println("----------------------------------");
        targetAOP.targetAround();
        System.out.println("----------------------------------");
        try {
            targetAOP.targetAfterThrowing(new RuntimeException("error"));
            targetAOP.targetAfterThrowing(null);
        } catch (Throwable throwable) {
        }
    }

}
