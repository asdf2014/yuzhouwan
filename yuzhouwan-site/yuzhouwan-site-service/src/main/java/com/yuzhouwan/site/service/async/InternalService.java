package com.yuzhouwan.site.service.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: com.yuzhouwan.site.async
 *
 * @author Benedict Jin
 * @since 2016/8/26
 */
@Component
//@Async
public class InternalService implements IInternalService {

    /**
     * interface + method level @async
     * class level @async
     */
    @Override
    public void longTimeOperationInternal(long seconds) {
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public void longTimeOperation(long seconds) {
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
