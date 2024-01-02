package com.yuzhouwan.hacker.snmp.v3;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：H3C User Target
 *
 * @author Benedict Jin
 * @since 2015/12/31
 */
public class H3CUserTarget {

    private final String address;
    private final String securityName2;
    private final int securityLevel;
    private final int securityModel;
    private final int retries;
    private final long timeout;
    private final int version;

    public H3CUserTarget(String address, String securityName2, int securityLevel,
                         int securityModel, int retries, long timeout, int version) {
        this.address = address;
        this.securityName2 = securityName2;
        this.securityLevel = securityLevel;
        this.securityModel = securityModel;
        this.retries = retries;
        this.timeout = timeout;
        this.version = version;
    }

    public String getAddress() {
        return address;
    }

    public String getSecurityName2() {
        return securityName2;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }

    public int getSecurityModel() {
        return securityModel;
    }

    public int getRetries() {
        return retries;
    }

    public long getTimeout() {
        return timeout;
    }

    public int getVersion() {
        return version;
    }
}
