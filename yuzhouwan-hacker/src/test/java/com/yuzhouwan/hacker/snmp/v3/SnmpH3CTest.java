package com.yuzhouwan.hacker.snmp.v3;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：SnmpH3C Tester
 *
 * @author Benedict Jin
 * @since 2015/12/31 0029
 */
public class SnmpH3CTest {

    private SnmpH3C snmpH3C;

    @Before
    public void before() {

        /**
         * snmp-agent target-host trap address udp-domain 192.168.112.155 udp-port 5000 params securityname yuzhouwan
         * <p/>
         * snmp-agent usm-user v3 yuzhouwan managev3group simple authentication-mode md5 helloyuzhouwan privacy-mode 3des helloyuzhouwan acl 2222
         */
        String securityName = "yuzhouwan2"; //"managev3group"; //"yuzhouwan"; //"yuzhouwan";
        AuthGeneric authenticationProtocol = new AuthMD5();
        String authenticationPassphrase = "helloyuzhouwan";
        PrivacyProtocol privacyProtocol = new PrivAES128();      //new Priv3DES();
        String privacyPassphrase = "helloyuzhouwan";

        H3CSnmpV3User h3CSnmpV3User = new H3CSnmpV3User(securityName,
                authenticationProtocol, authenticationPassphrase,
                privacyProtocol, privacyPassphrase);

        String userName = "yuzhouwan2";   //same as securityName ?

        String address = "192.168.6.201";
        String securityName2 = "yuzhouwan2"; //"managev3group"; //"yuzhouwan"; //"yuzhouwan";
        int securityLevel = SecurityLevel.AUTH_PRIV;
        int securityModel = 3;
        /*int maxSizeRequestPDU = '\uffff';*/
        int retries = 3;
        long timeout = 1000 * 10;
        int version = SnmpConstants.version3;

        H3CUserTarget h3CUserTarget = new H3CUserTarget(address,
                securityName2, securityLevel,
                securityModel, retries,
                timeout, version);

        H3CInfos h3CInfos = new H3CInfos(h3CSnmpV3User, userName, h3CUserTarget);

        snmpH3C = new SnmpH3C(h3CInfos);
    }

    @After
    public void after() {
        snmpH3C = null;
    }

    /**
     * Method: sendRequest()
     */
    @Test
    public void testSendRequest() {
        List<String> oidList = new ArrayList<>();
        oidList.add("1.3.6.1.2.1.1.5.0");       //device type: h3c
//        oidList.add("1.3.6");       // example in "http://www.snmp4j.org/doc/org/snmp4j/Snmp.html"
        snmpH3C.sendRequest(oidList);
    }
}
