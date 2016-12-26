package com.yuzhouwan.hacker.json;

import java.io.Serializable;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: A
 *
 * @author Benedict Jin
 * @since 2016/3/17
 */
public class A implements Serializable {
    public String mode;
    private String rule;
    private String groupId;
    private int level;

    public A() {
    }

    public A(String rule, String groupId, String mode, int level) {
        this.rule = rule;
        this.groupId = groupId;
        this.mode = mode;
        this.level = level;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
