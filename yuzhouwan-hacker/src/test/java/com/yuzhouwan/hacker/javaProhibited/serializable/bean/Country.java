package com.yuzhouwan.hacker.javaProhibited.serializable.bean;

import java.io.Serializable;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Country
 *
 * @author Benedict Jin
 * @since 2018/2/6
 */
public class Country implements Serializable {

    private String name;
    private transient String timezone;

    public Country() {
    }

    public Country(String name, String timezone) {
        this.name = name;
        this.timezone = timezone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", timezone='" + timezone + '\'' +
                '}';
    }
}
