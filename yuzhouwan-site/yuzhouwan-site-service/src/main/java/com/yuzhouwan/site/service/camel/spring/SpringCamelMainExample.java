package com.yuzhouwan.site.service.camel.spring;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Spring + Camel Example
 *
 * @author Benedict Jin
 * @since 2016/8/7
 */
public class SpringCamelMainExample {

    private static final Log _log = LogFactory.getLog(SpringCamelMainExample.class);

    public static void main(String[] args) throws Exception {

        /*
         * 需要了解在应用程序中如何进行Spring的加载、如果在Web程序中进行加载、如何在OSGI中间件中进行加载
         *
         * Camel会以SpringCamelContext类作为Camel上下文对象
         */
        ApplicationContext ap = new ClassPathXmlApplicationContext("classpath*:camel/spring.camel.xml");
        SpringCamelMainExample._log.info("初始化....." + ap);

        // 没有具体的业务含义，只是保证主线程不退出
        synchronized (SpringCamelMainExample.class) {
            SpringCamelMainExample.class.wait();
        }
    }
}
