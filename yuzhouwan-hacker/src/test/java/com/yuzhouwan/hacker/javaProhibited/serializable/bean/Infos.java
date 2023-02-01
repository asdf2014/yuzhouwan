package com.yuzhouwan.hacker.javaProhibited.serializable.bean;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Infos
 *
 * @author Benedict Jin
 * @since 2015/8/4
 */
public class Infos {

    private String tel;
    private transient String blog;

    public Infos() {
    }

    public Infos(String tel, String blog) {
        this.tel = tel;
        this.blog = blog;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    @Override
    public String toString() {
        return "Infos{" +
                "tel='" + tel + '\'' +
                ", blog='" + blog + '\'' +
                '}';
    }
}
