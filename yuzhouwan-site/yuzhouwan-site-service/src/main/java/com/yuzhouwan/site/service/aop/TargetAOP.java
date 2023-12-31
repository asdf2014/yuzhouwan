package com.yuzhouwan.site.service.aop;

import org.springframework.stereotype.Component;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Target AOP
 *
 * @author Benedict Jin
 * @since 2015/11/9
 */
@Component
public class TargetAOP {

    public void targetBefore() {
    }

    public void targetAfter() {
    }

    public boolean targetAfterReturning(boolean ret) {
        return ret;
    }

    public void targetAround() {
    }

    public void targetAfterThrowing(Throwable thr) throws Throwable {
        if (thr != null) throw thr;
    }
}
