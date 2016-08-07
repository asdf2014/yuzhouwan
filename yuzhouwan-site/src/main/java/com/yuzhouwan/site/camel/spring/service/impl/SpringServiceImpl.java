package com.yuzhouwan.site.camel.spring.service.impl;

import com.yuzhouwan.site.camel.spring.service.ISpringService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：SpringServiceImpl
 *
 * @author Benedict Jin
 * @since 2016/8/7
 */
@Component("SpringServiceImpl")
public class SpringServiceImpl implements ISpringService {

    /**
     * 实现了定义的DoSomethingService接口，并且交由Spring Ioc容器托管
     */
    private static final Log _log = LogFactory.getLog(SpringServiceImpl.class);

    @Override
    public void doSomething(String userId) {
        SpringServiceImpl._log.info("doSomething(String userId) ...");
    }
}