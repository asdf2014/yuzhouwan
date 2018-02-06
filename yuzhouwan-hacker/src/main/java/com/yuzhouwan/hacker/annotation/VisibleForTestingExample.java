package com.yuzhouwan.hacker.annotation;

import com.google.common.annotations.VisibleForTesting;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：VisibleForTesting Example
 *
 * @author Benedict Jin
 * @since 2018/2/6
 */
public class VisibleForTestingExample {

    @VisibleForTesting /*private*/ boolean visibleForTesting() {
        return true;
    }
}
