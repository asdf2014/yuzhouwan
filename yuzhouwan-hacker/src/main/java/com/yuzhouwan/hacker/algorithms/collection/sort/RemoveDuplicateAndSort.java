package com.yuzhouwan.hacker.algorithms.collection.sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: Remove Duplicate And Sort
 *
 * @author Benedict Jin
 * @since 2016/3/8
 */
public class RemoveDuplicateAndSort {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveDuplicateAndSort.class);

    private static final Random r0 = new Random(17);
    private static final Random r1 = new Random(34);
    private static final Random r2 = new Random(91);

    private static final Comparator<Company> comparator = (c0, c1) -> {

        double of = c0.getFoundation();
        double tf = c1.getFoundation();
        return Double.compare(tf, of);
    };

    private static final Comparator<Company> comparator4Equal = (c1, c2) -> {

        String city1 = c1.getCity();
        String city2 = c2.getCity();

        int result = city1.compareTo(city2);
//                _log.debug("{}-{}:{}", city1, city2, result);
        return result;
    };

    void example() {

        List<Company> companies = new ArrayList<>();
        companies.add(new Company("Washington", 2));
        companies.add(new Company("Washington", 1));
        companies.add(new Company("Washington", 4));
        companies.add(new Company("China", 3));
        companies.sort(comparator);
        for (Company company : companies) {
            LOGGER.debug(company.toString());
        }
        System.out.println("------------------------------------");
        List<Company> result = removeDuplicateUsers(companies, 1);
        System.out.println("------------------------------------");
        for (Company company : result) {
            LOGGER.debug(company.toString());
        }
    }

    /**
     * 100 0002, time:567 ms.
     */
    void performance() {

        List<Company> companies = new ArrayList<>();
        int count = 333334;
        while (count >= 0) {
            companies.add(new Company("Washington", r0.nextInt()));
            companies.add(new Company("China", r1.nextInt()));
            companies.add(new Company("UK", r2.nextInt()));
            count--;
        }
        long start = System.currentTimeMillis();
        companies.sort(comparator);
        List<Company> result = removeDuplicateUsers(companies, 2);
        long end = System.currentTimeMillis();
        for (Company company : result) {
            LOGGER.debug(company.toString());
        }
        LOGGER.debug("time:{} ms", end - start);
    }

    /**
     * @param companyIpResults
     * @param num              : limitation for topN
     * @return
     */
    private List<Company> removeDuplicateUsers(List<Company> companyIpResults, int num) {
        Set<Company> s = new TreeSet<>(comparator4Equal);
        for (Company companyIpResult : companyIpResults) {
            s.add(companyIpResult);
            /*
             * 16:17:49.563 [main] DEBUG c.y.c.RemoveDuplicateAndSort -
             * Company{city='UK', foundation=2.147477467E9}
             *
             * 16:17:49.565 [main] DEBUG c.y.c.RemoveDuplicateAndSort -
             * Company{city='Washington', foundation=2.147477037E9}
             */
            if (s.size() == num) break;
        }
        return new ArrayList<>(s);
    }

}
