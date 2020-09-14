package com.yuzhouwan.hacker.snmp.v3;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：snmp v3 user informations
 *
 * @author Benedict Jin
 * @since 2015/12/31
 */
public class H3CInfos {

    private String userName4USM;
    private H3CSnmpV3User h3CSnmpV3User;
    private H3CUserTarget h3CUserTarget;

    public H3CInfos(H3CSnmpV3User h3CSnmpV3User, String userName4USM, H3CUserTarget h3CUserTarget) {
        this.h3CSnmpV3User = h3CSnmpV3User;
        this.userName4USM = userName4USM;
        this.h3CUserTarget = h3CUserTarget;
    }

    public H3CSnmpV3User getH3CSnmpV3User() {
        return h3CSnmpV3User;
    }

    public String getUserName4USM() {
        return userName4USM;
    }

    public H3CUserTarget getH3CUserTarget() {
        return h3CUserTarget;
    }
}
