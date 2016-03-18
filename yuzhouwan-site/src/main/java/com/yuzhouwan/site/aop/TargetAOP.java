package com.yuzhouwan.site.aop;

import org.springframework.stereotype.Component;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：spring.aop
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

    public boolean targetAfterReturning(boolean _return) {
        return _return;
    }

    public void targetAround() {
    }

    public void targetAfterThrowing(Throwable _throw) throws Throwable {
        if (_throw == null)
            return;
        else
            throw _throw;
    }
}
