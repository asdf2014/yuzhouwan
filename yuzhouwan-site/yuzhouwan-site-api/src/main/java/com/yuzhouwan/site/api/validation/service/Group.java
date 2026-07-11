package com.yuzhouwan.site.api.validation.service;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: Group
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
@GroupSequence({Default.class, GroupA.class, GroupB.class})
public interface Group {
}
