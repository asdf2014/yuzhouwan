package snmp.simple;

import org.junit.Before;
import org.junit.Test;
import snmp.acl.SnmpSet;

import java.util.LinkedList;
import java.util.List;

/**
 * Copyright @ yuzhouwan.com
 * All right reserved.
 * Function：SnmpSimpleGet Tester.
 *
 * @author asdf2014
 * @since 2015/11/19
 */
public class SnmpSimpleGetTest {

    private static final String ip = "192.168.1.201";

    private static final String percentage_of_user_CPU_time = ".1.3.6.1.4.1.2021.11.50.0";
    private static final String raw_user_cpu_time = ".1.3.6.1.4.1.2021.11.50.0";
    private static final String percentages_of_system_CPU_time = ".1.3.6.1.4.1.2021.11.10.0";
    private static final String raw_system_cpu_time = ".1.3.6.1.4.1.2021.11.52.0";
    private static final String port = "1.3.6.1.2.1.31.1.1.1.1";

    private static final String sysORDescr1 = "1.3.6.1.2.1.1.9.1.3.1";

    private static final String errorCount = "1.3.6.1.2.1.25.3.2.1.6.262145";

    private List<String> oids;

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
    @Test
    public void testSnmpSyncGetList() throws Exception {

        /**
         * TODO{jinjy}: make sure snmp.acl is invisible for user
         */
        /**
         * JVM Options:com.
         * -Dcom.sun.management.snmp.trap=162
         * -Dcom.sun.management.snmp.acl=true
         * -Dcom.sun.management.snmp.acl.file=resources/snmp.acl
         */

        SnmpSimpleGet.snmpSyncGetList(ip, "public", oids);
        try {
            snmpV2sysORDescr1();
//            snmpV2SetPort();
//            snmpV2Set();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        snmpV3Set();
    }

    private void snmpV2sysORDescr1() {
        new SnmpSet(ip, errorCount + "={i}123").doSet();
        SnmpSimpleGet.snmpSyncGetList(ip, "public", oids);
    }

    private void snmpV2SetPort() {
        new SnmpSet(ip, port + "={i}9999").doSet();
        SnmpSimpleGet.snmpSyncGetList(ip, "public", oids);
        new SnmpSet(ip, port + "={u}9999").doSet();
        SnmpSimpleGet.snmpSyncGetList(ip, "public", oids);
        new SnmpSet(ip, port + "={s}9999").doSet();
        SnmpSimpleGet.snmpSyncGetList(ip, "public", oids);
        new SnmpSet(ip, port + "={x}9999").doSet();
        SnmpSimpleGet.snmpSyncGetList(ip, "public", oids);
        new SnmpSet(ip, port + "={d}9999").doSet();
        SnmpSimpleGet.snmpSyncGetList(ip, "public", oids);
//        new SnmpSet(ip, port + "={b}9999").doSet();
        new SnmpSet(ip, port + "={n}9999").doSet();
        SnmpSimpleGet.snmpSyncGetList(ip, "public", oids);
        new SnmpSet(ip, port + "={o}9999").doSet();
        SnmpSimpleGet.snmpSyncGetList(ip, "public", oids);
        new SnmpSet(ip, port + "={t}9999").doSet();
        SnmpSimpleGet.snmpSyncGetList(ip, "public", oids);
        new SnmpSet(ip, port + "={a}9999").doSet();
        SnmpSimpleGet.snmpSyncGetList(ip, "public", oids);
    }

    private void snmpV2Set() {
        System.out.println("Doing SNMPv2 set..");
        new SnmpSet(ip, percentage_of_user_CPU_time + "={i}1").doSet();
        new SnmpSet(ip, percentage_of_user_CPU_time + "={u}1").doSet();
        new SnmpSet(ip, percentage_of_user_CPU_time + "={s}1").doSet();
        new SnmpSet(ip, percentage_of_user_CPU_time + "={x}1").doSet();
        new SnmpSet(ip, percentage_of_user_CPU_time + "={d}1").doSet();
        new SnmpSet(ip, percentage_of_user_CPU_time + "={b}1").doSet();
        new SnmpSet(ip, percentage_of_user_CPU_time + "={n}1").doSet();
        new SnmpSet(ip, percentage_of_user_CPU_time + "={o}1").doSet();
        new SnmpSet(ip, percentage_of_user_CPU_time + "={t}1").doSet();
        new SnmpSet(ip, percentage_of_user_CPU_time + "={a}1").doSet();
    }

    private void snmpV3Set() {
        SnmpSet set;
        System.out.println("Doing SNMPv3 set..");
        set = new SnmpSet(ip,
                "1.3.6.1.2.1.1.6.0={s}Some place else..",
                "kschmidt", "MD5", "mysecretpass", "DES", "mypassphrase");
        set.doSet();
    }
}
