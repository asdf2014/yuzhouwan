package com.yuzhouwan.common.util;

import java.util.Properties;

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
    }

    public long getModify() {
        return modify;
    }

    public void setModify(long modify) {
        this.modify = modify;
    }
}
