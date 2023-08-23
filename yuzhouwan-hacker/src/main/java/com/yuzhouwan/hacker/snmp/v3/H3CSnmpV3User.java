package com.yuzhouwan.hacker.snmp.v3;

import org.snmp4j.security.AuthGeneric;
import org.snmp4j.security.PrivacyProtocol;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：H3C Snmp V3 User
 *
 * @author Benedict Jin
 * @since 2015/12/31
 */
public class H3CSnmpV3User {

    private final String securityName;
    private final AuthGeneric authenticationProtocol;
    private final String authenticationPassphrase;
    private final PrivacyProtocol privacyProtocol;
    private final String privacyPassphrase;

    public H3CSnmpV3User(String securityName, AuthGeneric authenticationProtocol,
                         String authenticationPassphrase, PrivacyProtocol privacyProtocol, String privacyPassphrase) {
        this.securityName = securityName;
        this.authenticationProtocol = authenticationProtocol;
        this.authenticationPassphrase = authenticationPassphrase;
        this.privacyProtocol = privacyProtocol;
        this.privacyPassphrase = privacyPassphrase;
    }

    public String getSecurityName() {
        return securityName;
    }

    public AuthGeneric getAuthenticationProtocol() {
        return authenticationProtocol;
    }

    public String getAuthenticationPassphrase() {
        return authenticationPassphrase;
    }

    public PrivacyProtocol getPrivacyProtocol() {
        return privacyProtocol;
    }

    public String getPrivacyPassphrase() {
        return privacyPassphrase;
    }
}
