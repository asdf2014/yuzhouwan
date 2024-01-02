package com.yuzhouwan.hacker.algorithms.collection.sort;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: Company
 *
 * @author Benedict Jin
 * @since 2016/3/8
 */
public class Company implements Comparable<Company> {

    private String city;
    private double foundation;

    public Company(String city, double foundation) {
        this.city = city;
        this.foundation = foundation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getFoundation() {
        return foundation;
    }

    public void setFoundation(double foundation) {
        this.foundation = foundation;
    }

    public int compareTo(Company o) {
        double of = o.getFoundation();
        double tf = this.getFoundation();
        return Double.compare(of, tf);
    }

    @Override
    public String toString() {
        return "Company{" + "city='" + city + '\'' + ", foundation=" + foundation + '}';
    }
}

