package com.yuzhouwan.site.service.camel.spring;

import com.yuzhouwan.site.api.camel.service.ISpringService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Apache Camel Processor Example
 *
 * @author Benedict Jin
 * @since 2016/8/7
 */
@Component("processorExample")
public class ProcessorExample implements Processor {

    /**
     * 自定义的处理器，处理器本身交由Spring Ioc容器管理
     * 并且其中注入了一个DoSomethingService接口的实现.
     */
    private static final Log _log = LogFactory.getLog(ProcessorExample.class);

    @Autowired
    private ISpringService somethingService;

    @Override
    public void process(Exchange exchange) {
        // 调用somethingService，说明它正常工作
        this.somethingService.doSomething("yuzhouwan");
        // 这里在控制台打印一段日志，证明这个Processor正常工作了，就行
        ProcessorExample._log.info("process(Exchange exchange) ... ");
    }
}
