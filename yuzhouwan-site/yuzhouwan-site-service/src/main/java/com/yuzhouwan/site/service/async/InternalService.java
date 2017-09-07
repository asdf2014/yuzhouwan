package com.yuzhouwan.site.service.async;

import com.yuzhouwan.site.api.async.service.IInternalService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: Internal Service
 *
 * @author Benedict Jin
 * @since 2016/8/26
 */
@Component
//@Async
public class InternalService implements IInternalService {

    /**
     * interface + method level @async.
     * class level @async.
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
