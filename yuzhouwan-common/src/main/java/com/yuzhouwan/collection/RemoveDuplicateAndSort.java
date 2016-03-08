package com.yuzhouwan.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Remove Duplicate And Sort
 *
 * @author Benedict Jin
 * @since 2016/3/8 0030
 */
public class RemoveDuplicateAndSort {

    private static final Logger _log = LoggerFactory.getLogger(RemoveDuplicateAndSort.class);
    private static Random r0 = new Random(17);
    private static Random r1 = new Random(34);

    private static Comparator comparator = new Comparator<Company>() {

        public int compare(Company c0, Company c1) {

            double of = c0.getFoundition();
            double tf = c1.getFoundition();
            if (of > tf) {
                return -1;
            } else if (of < tf) {
                return 1;
            } else {
                return 0;
            }
        }
    };

    public void example() {

        List<Company> companies = new ArrayList<Company>();
        companies.add(new Company("Washington", 2));
        companies.add(new Company("Washington", 1));
        companies.add(new Company("Washington", 4));
        companies.add(new Company("China", 3));
        Collections.sort(companies, comparator);
        for (Company company : companies) {
            _log.debug(company.toString());
        }
        System.out.println("------------------------------------");
        List<Company> result = removeDuplicteUsers(companies);
        System.out.println("------------------------------------");
        for (Company company : result) {
            _log.debug(company.toString());
        }
    }

    /**
     * time:1521
     */
    public void performance() {

        List<Company> companies = new ArrayList<Company>();
        int count = 1000000;
        while (count > 0) {
            companies.add(new Company("Washington", r0.nextInt()));
            companies.add(new Company("China", r1.nextInt()));
            count--;
        }
        long start = System.currentTimeMillis();
        Collections.sort(companies, comparator);
        List<Company> result = removeDuplicteUsers(companies);
        long end = System.currentTimeMillis();
        for (Company company : result) {
            _log.debug(company.toString());
        }
        _log.debug("time:{}", end - start);
    }

    private List<Company> removeDuplicteUsers(List<Company> companyIpResults) {
        Set<Company> s = new TreeSet<Company>(new Comparator<Company>() {

            @Override
            public int compare(Company c1, Company c2) {
                String city1 = c1.getCity();
                String city2 = c2.getCity();

                int result = city1.compareTo(city2);
//                _log.debug("{}-{}:{}", city1, city2, result);
                return result;
            }
        });
        s.addAll(companyIpResults);
        return new ArrayList<Company>(s);
    }

}
