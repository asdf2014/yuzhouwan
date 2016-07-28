package com.yuzhouwan.common.collection;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Company
 *
 * @author Benedict Jin
 * @since 2016/3/8 0030
 */
public class Company implements Comparable<Company> {

    private String city;
    private double foundition;

    public Company(String city, double foundition) {
        this.city = city;
        this.foundition = foundition;
    }

    public String getCity() {
        return city;
    }

    public double getFoundition() {
        return foundition;
    }

    public int compareTo(Company o) {
        double of = o.getFoundition();
        double tf = this.getFoundition();
        if (of > tf) {
            return 1;
        } else if (of < tf) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Company{" +
                "city='" + city + '\'' +
                ", foundition=" + foundition +
                '}';
    }
}

