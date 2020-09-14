package com.yuzhouwan.hacker.lambda;

import com.alibaba.fastjson.JSON;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：Apple
 *
 * @author Benedict Jin
 * @since 2018/3/5
 */
public class Apple {

    private String name;
    private String color;
    private Double weight;

    Apple(String name, String color, Double weight) {
        this.name = name;
        this.color = color;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
