package com.yuzhouwan.hacker.regular;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Group in Regular
 *
 * @author Benedict Jin
 * @since 2016/3/28 0001
 */
public class GroupTry {

    /**
     * Feb 23 11:09:17 2016 GX-NN-SR-1.D.S5820 %%10SSHS/6/SSHLOG: -DevIP=116.1.239.33; User lianghb logged out from 219.143.200.182 port 65164.
     */
    @Test
    public void simple() {

        /**
         * Group's Count:	5
         *
         * Time:	Feb 23 11:09:17 2016
         * Device:	GX-NN-SR-1.D.S5820
         * Oper:	%%10SSHS/6/SSHLOG
         * Body:	-DevIP=116.1.239.33; User lianghb logged out from 219.143.200.182 port 65164.
         */
        String logGuangxi = "Feb 23 11:09:17 2016 GX-NN-SR-1.D.S5820 %%10SSHS/6/SSHLOG: -DevIP=116.1.239.33; User lianghb logged out from 219.143.200.182 port 65164.";
        parse(logGuangxi);
    }

    private void parse(String expression) {
        Pattern p = Pattern.compile("(?<time>(\\S+ +){4})(?<device>\\S+ )(?<operator>\\S+):(?<body>.*)");
        Matcher m = p.matcher(expression);
        if (m.find()) {
            System.out.println("Group's Count:\t" + m.groupCount() + "\r\n");
            System.out.println("Time:\t" + m.group("time").trim());
            System.out.println("Device:\t" + m.group("device").trim());
            System.out.println("Oper:\t" + m.group("operator").trim());
            System.out.println("Body:\t" + m.group("body").trim());
        }
    }

    /**
     * %Mar  1 17:07:42:807 2016 CT_GX-NN-HMP_SR-1.S5560 IFNET/5/LINK_UPDOWN: Line protocol on the interface Ten-GigabitEthernet1/1/1 is down.
     */
    @Test
    public void simple2() {

        /**
         * Group's Count:	5
         *
         * Time:	%Mar  1 17:07:42:807 2016
         * Device:	CT_GX-NN-HMP_SR-1.S5560
         * Oper:	IFNET/5/LINK_UPDOWN
         * Body:	Line protocol on the interface Ten-GigabitEthernet1/1/1 is down.
         */
        String logHuangMaoPing = "%Mar  1 17:07:42:807 2016 CT_GX-NN-HMP_SR-1.S5560 IFNET/5/LINK_UPDOWN: Line protocol on the interface Ten-GigabitEthernet1/1/1 is down.";
        parse(logHuangMaoPing);
    }

    @Test
    public void groupTry() {
        Pattern p = Pattern.compile("^(\\d+)([￥$])$");
        String str = "8899￥";
        Matcher m = p.matcher(str);
        if (m.matches()) {
            System.out.println("货币金额: " + m.group(1));
            System.out.println("货币种类: " + m.group(2));
        }
    }

    @Test
    public void testDateFormat() {
        String str = "%Mar  1 17:07:42:807 2016";
        SimpleDateFormat sdf = new SimpleDateFormat("%MMM  d HH:mm:ss:SSS yyyy", Locale.ENGLISH);
        try {
            System.out.println(sdf.parse(str));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDateFormat2() {
        String str = "1 17:07:42:807 2016";
        SimpleDateFormat sdf = new SimpleDateFormat("d HH:mm:ss:SSS yyyy", Locale.ENGLISH);
        try {
            System.out.println(sdf.parse(str));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSplit() {
        String str = "IFNET/5/LINK_UPDOWN";
        for (String s : str.split("/")) {
            System.out.println(s);
        }
    }

    @Test
    public void testGroup() {
        String str = "2017-01-12 07:00:00,648 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem.audit: allowed=true ugi=aps (auth:SIMPLE) ip=/10.27.236.67 cmd=listStatus src=/user/aps/admds/priceTextRealtime/aa/bb dst=null perm=null";
        Pattern p = Pattern.compile("(?<=ugi=)(?<ugi>\\w+).*(?<= src=)(?<src>.*(?= dst))");
        Matcher m = p.matcher(str);
        assertEquals(true, m.find());
        assertEquals("aps", m.group(1));
        assertEquals("/user/aps/admds/priceTextRealtime/aa/bb", m.group("src"));
    }
}
