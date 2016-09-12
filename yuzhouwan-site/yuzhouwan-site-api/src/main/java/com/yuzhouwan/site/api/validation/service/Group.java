package com.yuzhouwan.site.api.validation.service;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, GroupA.class, GroupB.class})
public interface Group {
}