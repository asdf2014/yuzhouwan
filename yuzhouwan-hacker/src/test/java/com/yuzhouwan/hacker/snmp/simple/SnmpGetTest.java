package com.yuzhouwan.hacker.snmp.simple;

import java.util.LinkedList;
import java.util.List;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：SnmpSimpleGet Tester.
 *
 * @author Benedict Jin
 * @since 2015/11/12
 */
public class SnmpGetTest {

    //    @Test
    public void testSnmpGet() {

        String ip = "192.168.1.201";
        String community = "public";

        String percentage_of_user_CPU_time = ".1.3.6.1.4.1.2021.11.50.0";
        String raw_user_cpu_time = ".1.3.6.1.4.1.2021.11.50.0";
        String percentages_of_system_CPU_time = ".1.3.6.1.4.1.2021.11.10.0";
        String raw_system_cpu_time = ".1.3.6.1.4.1.2021.11.52.0";

        List<String> oids = new LinkedList<>();
        oids.add(percentage_of_user_CPU_time);
        oids.add(raw_user_cpu_time);
        oids.add(percentages_of_system_CPU_time);
        oids.add(raw_system_cpu_time);

        SnmpSimpleGet.snmpGet(ip, community, oids);
    }

    //    @Test
    public void testSnmpSyncGet() {

        String ip = "192.168.1.201";
        String community = "public";

        String percentage_of_user_CPU_time = ".1.3.6.1.4.1.2021.11.50.0";
        String raw_user_cpu_time = ".1.3.6.1.4.1.2021.11.50.0";
        String percentages_of_system_CPU_time = ".1.3.6.1.4.1.2021.11.10.0";
        String raw_system_cpu_time = ".1.3.6.1.4.1.2021.11.52.0";

        List<String> oids = new LinkedList<>();
        oids.add(percentage_of_user_CPU_time);
        oids.add(raw_user_cpu_time);
        oids.add(percentages_of_system_CPU_time);
        oids.add(raw_system_cpu_time);

        SnmpSimpleGet.snmpSyncGetList(ip, community, oids);
    }
}
