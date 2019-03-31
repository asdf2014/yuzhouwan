package com.yuzhouwan.hacker.snmp.acl;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：SnmpSet
 *
 * @author Benedict Jin
 * @since 2015/11/30
 */
public class SnmpSet {

    private SnmpUtil _util;

    public SnmpSet(String host, String varbind) {
        _util = new SnmpUtil(host, varbind, false, 0);
    }

    public SnmpSet(String host, String varbind, String user, String authProtocol,
                   String authPasshrase, String privProtocol, String privPassphrase) {

        _util = new SnmpUtil(host, varbind, user, authProtocol,
                authPasshrase, privProtocol, privPassphrase, false, 0);
    }

    public void doSet() {
        _util.sendAndProcessResponse();
    }
}
