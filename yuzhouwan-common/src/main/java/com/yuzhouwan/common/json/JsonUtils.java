package com.yuzhouwan.common.json;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Make some simple example for fastJSON
 *
 * @author Benedict Jin
 * @since 2016/3/17 0030
 */
public class JsonUtils {

    public List<A> simpleParse() {
        String s = "[{\"rule\":\"is down\", \"groupId\":\"1\",\"mode\":\"WECHAT\", \"level\":\"1\"}]";
        return JSON.parseArray(s, A.class);
    }

}

class A {
    private String rule;
    private String groupId;
    private String mode;
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
