package snmp.v3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：Get the value of oid from h3c telnet using snmp v3 user by snmp4j
 *
 * @author jinjy
 * @since 2015/12/25 0025
 */
public class SnmpTelnetH3C {

    private static final Logger _log = LoggerFactory.getLogger(SnmpTelnetH3C.class);

    //    private static final OctetString PRIV = new OctetString("privUser");
    private static final OctetString PRIV = new OctetString("authPriv");
//    private static final OctetString PRIV_CONTEXT_NAME = new OctetString("priv");

    /**
     * snmp-agent usm-user v3 admin public simple authentication-mode md5 123456789 privacy-mode 3des 123456789 acl 2222
     */
    private static final OctetString USER_NAME = new OctetString("admin");
    private static final OID AUTH_PROTOCOL_MD5 = AuthMD5.ID;
    private static final OctetString AUTH_PASS = new OctetString("123456789");
    private static final OID PRIVATE_PROTOCOL_3DES = Priv3DES.ID;
    private static final OctetString PRIVATE_PASS = new OctetString("123456789");

    /**
     * base informations
     */
    public static final String DEFAULT_PROTOCOL = "udp";
    public static final int DEFAULT_PORT = 161;
    private static final String ip = "192.168.6.201";

    public void showDeviceType() {
        OID deviceType = new OID("1.3.6.1.2.1.1.5.0");
        Address address = GenericAddress.parse(DEFAULT_PROTOCOL + ":" + ip
                + "/" + DEFAULT_PORT);
        getOIDValueV3(deviceType, address);
    }

    /**
     * authentication and privacy
     *
     * @param oid
     * @param address
     */
    public void getOIDValueV3(OID oid, Address address) {
        try {
            long startTime = System.currentTimeMillis();

            Snmp snmp = createSnmp(PRIV,
                    AUTH_PROTOCOL_MD5, AUTH_PASS,
                    PRIVATE_PROTOCOL_3DES, PRIVATE_PASS,
                    USER_NAME);
            _log.info(snmp.getUSM().getUserTable().getUser(PRIV).toString());
            snmp.listen();
            _log.info("snmp listening...");


            ScopedPDU pdu = new ScopedPDU();
            VariableBinding var = new VariableBinding(oid);
            pdu.add(var);
//            pdu.setContextName(PRIV_CONTEXT_NAME);
            pdu.setContextEngineID(USER_NAME);
            pdu.setType(PDU.GET);

            UserTarget myTarget = new UserTarget();
            myTarget.setAddress(address);
            myTarget.setVersion(SnmpConstants.version3);
            myTarget.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            myTarget.setSecurityName(USER_NAME);

            ResponseEvent response = snmp.send(pdu, myTarget);
            _log.info("sending request...");
            if (response != null) {
                _log.info("response: {}", response);
                PDU responsePdu = response.getResponse();
                if (responsePdu != null)
                    _log.info("Response (device type): {}", responsePdu.toString());
                else
                    _log.error("Response (device type): null");
                Exception error = response.getError();
                if (error != null)
                    _log.error("Error: {}", error.toString());
                else
                    _log.info("Error: null.");
            } else {
                _log.error("Response is null!!!");
            }
            _log.info("The cost time for snmp v3: {}", (System.currentTimeMillis() - startTime));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Snmp createSnmp(OctetString securityName,
                            OID authProtocol, OctetString authPass,
                            OID privacyProtocol, OctetString privacyPass, OctetString userName) throws IOException {
        _log.info("create snmp...");
        Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
        USM usm = new USM(SecurityProtocols.getInstance(),
                new OctetString(MPv3.createLocalEngineID()), 0);    //v3, something else MPv1/MPv2c
        SecurityModels.getInstance().addSecurityModel(usm);

        UsmUser user = new UsmUser(securityName,
                authProtocol, authPass,
                privacyProtocol, privacyPass);
        snmp.getUSM().addUser(userName, user);
        return snmp;
    }

    public Snmp createSnmpV3(String securityName, int v3_ap, String authPassphrase, int v3_privacy, String privacyPassphrase) throws IOException {

        DefaultUdpTransportMapping defaultUdpTransportMapping = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(defaultUdpTransportMapping);


        // SNMP V3
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        OID ap_oid = null;// 认证协议[MD5|SHA]
        if (v3_ap == 1) {
            // MD5
            ap_oid = AuthMD5.ID;
        } else if (v3_ap == 2) {
            // SHA
            ap_oid = AuthSHA.ID;
        }
        OID v3_privacy_oid = null;// 加密协议[DES|AES128|AES192|AES256]
        if (v3_privacy == 1) {
            // DES
            v3_privacy_oid = PrivDES.ID;
        } else if (v3_privacy == 2) {
            // AES128
            v3_privacy_oid = PrivAES128.ID;
        } else if (v3_privacy == 3) {
            // AES192
            v3_privacy_oid = PrivAES192.ID;
        } else if (v3_privacy == 4) {
            // AES256
            v3_privacy_oid = PrivAES256.ID;
        } else if (v3_privacy == 5) {
            // 3DES
            v3_privacy_oid = Priv3DES.ID;
        }
        // add user to the USM
        snmp.getUSM().addUser(new OctetString(securityName),
                new UsmUser(new OctetString(securityName),
                        ap_oid, //认证协议  MD5/SHA
                        new OctetString(authPassphrase),
                        v3_privacy_oid, //加密协议  DES/AES128/AES192/AES256/3DES
                        new OctetString(privacyPassphrase)));
        return snmp;
    }

    public UserTarget createUserTarget(String ipAddress, String securityName, int securityLevel, int retries, int timeout) {
        //SysLogger.info(address+"==="+community+"==="+version);
        UserTarget target = new UserTarget();
        target.setAddress(GenericAddress.parse(/*"udp:" + */ipAddress + "/161"));
        target.setRetries(retries);
        target.setTimeout(timeout);
        target.setVersion(SnmpConstants.version3);
        if (securityLevel == 1) {
            target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
        } else if (securityLevel == 2) {
            target.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);
        } else if (securityLevel == 3) {
            target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        }

        //target.setSecurityName(new OctetString("dhccsnmpv3"));
        target.setSecurityName(new OctetString(securityName));
        return target;
    }
}
