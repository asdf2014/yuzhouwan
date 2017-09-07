package com.yuzhouwan.hacker.snmp.simple;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: SnmpSimpleSet Tester.
 *
 * @author Benedict Jin
 * @since 2015/11/24 0024
 */
public class SnmpSimpleSetTest {

    private static final String ip = "192.168.1.201";

    private static final String percentage_of_user_CPU_time = ".1.3.6.1.4.1.2021.11.50.0";
    private static final String raw_user_cpu_time = ".1.3.6.1.4.1.2021.11.50.0";
    private static final String percentages_of_system_CPU_time = ".1.3.6.1.4.1.2021.11.10.0";
    private static final String raw_system_cpu_time = ".1.3.6.1.4.1.2021.11.52.0";
    private static final String port = "1.3.6.1.2.1.31.1.1.1.1";

    private static final String sysORDescr1 = "1.3.6.1.2.1.1.9.1.3.1";

    private static final String errorCount = "1.3.6.1.2.1.25.3.2.1.6.262145";

    private List<String> oids;

    @Ignore
    @Before
    public void init() {

        oids = new LinkedList<>();
//        oids.add(percentage_of_user_CPU_time);
//        oids.add(raw_user_cpu_time);
//        oids.add(percentages_of_system_CPU_time);
//        oids.add(raw_system_cpu_time);
//        oids.add(port);
//        oids.add(sysORDescr1);

        oids.add(errorCount);
    }

    /**
     * Method: snmpSyncSetList(String ip, String community, List<String> oidList)
     */
    @Ignore
    @Test
    public void testSnmpSyncSetList() throws Exception {

        SnmpSimpleSet.snmpSyncSetList(ip, "public", errorCount);
        SnmpSimpleGet.snmpSyncGetList(ip, "public", oids);
    }
}
