package com.yuzhouwan.common.json;

import java.io.Serializable;

public class A implements Serializable {
    private String rule;
    private String groupId;
    public String mode;
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
