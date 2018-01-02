package com.yuzhouwan.hacker.design.pattern;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Enum Single Instance for (De)Serialize
 *
 * @author Benedict Jin
 * @since 2016/8/3
 */
public enum SingleInstance {

    INSTANCE(3);

    private Integer point;

    SingleInstance(Integer point) {
        this.point = point;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
