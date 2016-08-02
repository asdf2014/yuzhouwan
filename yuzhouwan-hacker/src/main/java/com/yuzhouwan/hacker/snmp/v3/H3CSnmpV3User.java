package com.yuzhouwan.hacker.snmp.v3;

import org.snmp4j.security.AuthGeneric;
import org.snmp4j.security.PrivacyProtocol;

public class H3CSnmpV3User {

    private String securityName;
    private AuthGeneric authenticationProtocol;
    private String authenticationPassphrase;
    private PrivacyProtocol privacyProtocol;
    private String privacyPassphrase;

    public H3CSnmpV3User(String securityName, AuthGeneric authenticationProtocol, String authenticationPassphrase, PrivacyProtocol privacyProtocol, String privacyPassphrase) {
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
