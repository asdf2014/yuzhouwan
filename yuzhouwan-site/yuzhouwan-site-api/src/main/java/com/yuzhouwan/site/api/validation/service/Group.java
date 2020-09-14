package com.yuzhouwan.site.api.validation.service;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function: Group
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
@GroupSequence({Default.class, GroupA.class, GroupB.class})
public interface Group {
}
