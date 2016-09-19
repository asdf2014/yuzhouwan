package com.yuzhouwan.site.api.async.service;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: com.yuzhouwan.site.async
 *
 * @author Benedict Jin
 * @since 2016/8/26
 */
public interface IInternalService {

    void longTimeOperationInternal(long seconds);

    void longTimeOperation(long seconds);
}
