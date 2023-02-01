package com.yuzhouwan.site.service.async;

import com.yuzhouwan.site.api.async.service.IInternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Async Annotation
 *
 * @author Benedict Jin
 * @since 2016/8/26
 */
@Component
public class AsyncAnnotation {

    @Autowired
    private IInternalService internalService;

    void longTimeOperationInternal(long millisecond) {
        internalService.longTimeOperationInternal(millisecond);
    }

    void longTimeOperation(long millisecond) {
        internalService.longTimeOperation(millisecond);
    }

    /**
     * not work, should with interface declare.
     */
    @Async
    void longTimeOperationOrigin(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
