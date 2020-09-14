package com.yuzhouwan.common.util;

import java.util.Properties;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function: Prop
 *
 * @author Benedict Jin
 * @since 2016/4/8
 */
class Prop {

    private Properties p;
    private long modify;

    // Necessary: default constructor for FastJson
    public Prop() {
    }

    public Prop(Properties p, long modify) {
        this.p = p;
        this.modify = modify;
    }

    public Properties getP() {
        return p;
    }

    public void setP(Properties p) {
        this.p = p;
        updateModify();
    }

    public long getModify() {
        return modify;
    }

    public void setModify(long modify) {
        this.modify = modify;
    }

    public void updateModify() {
        this.modify = System.nanoTime();
    }
}
