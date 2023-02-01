package com.yuzhouwan.hacker.algorithms.reflection;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：reflection
 *
 * @author Benedict Jin
 * @since 2015/11/16
 */
public class SimpleBean {

    private Integer i;
    private Long l;
    private String s;

    public SimpleBean() {
    }

    public SimpleBean(Integer i, Long l, String s) {
        this.i = i;
        this.l = l;
        this.s = s;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public Long getL() {
        return l;
    }

    public void setL(Long l) {
        this.l = l;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return "SimpleBean{" + "i=" + i + ", l=" + l + ", s='" + s + '\'' + '}';
    }
}
