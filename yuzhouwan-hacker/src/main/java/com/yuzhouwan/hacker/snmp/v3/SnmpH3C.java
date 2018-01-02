package com.yuzhouwan.hacker.snmp.v3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.*;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Snmp H3C
 *
 * @author Benedict Jin
 * @since 2015/12/29
 */
public class SnmpH3C {

    private static final Logger LOG = LoggerFactory.getLogger(SnmpH3C.class);

    private Snmp snmp;

    private UsmUser usmUser;

    private UserTarget userTarget;
    private ScopedPDU pdu;

    private H3CInfos h3CInfos;

    public SnmpH3C(H3CInfos h3CInfos) {
        this.h3CInfos = h3CInfos;
    }

    /**
     * Send Request to H3C.
     *
     * @param oidList
     */
    public void sendRequest(List<String> oidList) {

        prepare();

        LOG.info("Adding oids into PDU...");
        addOIDs2PDU(oidList);
        try {
            LOG.info("Sending userTarget...");
            sendGetPDU(snmp, userTarget, pdu);
        } catch (Exception e) {
            LOG.error("Sending userTarget is failed");
        }
        try {
            snmp.close();
        } catch (IOException e) {
            LOG.error("Cannot close snmp");
        }
    }

    /**
     * Do some prepared works.
     */
    private void prepare() {
        createSNMP();
        createUserByH3CInfos();
        addUserIntoSNMP(h3CInfos.getUserName4USM());
        createUserTargetByH3CInfos();
    }

    /**
     * Create SNMP.
     */
    private void createSNMP() {

        LOG.info("Creating SNMP...");
        try {
            TransportMapping transport = new DefaultUdpTransportMapping();
            transport.listen();
            snmp = new Snmp(transport);

            // SNMP V3
            USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
            SecurityModels.getInstance().addSecurityModel(usm);

            LOG.debug("Snmp created.");
            snmp.listen();
            LOG.debug("Snmp listening...");
        } catch (IOException e) {
            throw new RuntimeException("Cannot create snmp!!!", e);
        }
    }

    /**
     * Create user by those informations from H3CInfos.H3CSnmpV3User.
     */
    private void createUserByH3CInfos() {
        LOG.info("Creating snmp v3 USM user...");
        H3CSnmpV3User h3CSnmpV3User = h3CInfos.getH3CSnmpV3User();

        String securityName = h3CSnmpV3User.getSecurityName();
        AuthGeneric authenticationProtocol = h3CSnmpV3User.getAuthenticationProtocol();
        String authenticationPassphrase = h3CSnmpV3User.getAuthenticationPassphrase();
        PrivacyProtocol privacyProtocol = h3CSnmpV3User.getPrivacyProtocol();
        String privacyPassphrase = h3CSnmpV3User.getPrivacyPassphrase();

        createUser(securityName, authenticationProtocol, authenticationPassphrase,
                privacyProtocol, privacyPassphrase);
    }

    /**
     * @param securityName
     * @param authenticationProtocol   AuthMD5 | AuthSHA
     * @param authenticationPassphrase
     * @param privacyProtocol          PrivDES | PrivAES128 | PrivAES192 | PrivAES256 | Priv3DES | PrivAES
     * @param privacyPassphrase
     * @return
     */
    private void createUser(String securityName,
                            AuthGeneric authenticationProtocol, String authenticationPassphrase,
                            PrivacyProtocol privacyProtocol, String privacyPassphrase) {

        this.usmUser = new UsmUser(new OctetString(securityName),
                authenticationProtocol.getID(), new OctetString(authenticationPassphrase),
                privacyProtocol.getID(), new OctetString(privacyPassphrase));
    }

    /**
     * Add user into SNMP's USM.
     *
     * @param userName
     */
    private void addUserIntoSNMP(String userName) {

        if (usmUser != null) {
            if (snmp != null) {
                snmp.getUSM().addUser(new OctetString(userName), this.usmUser);
            } else {
                LOG.error("Please create a instance of SNMP firstly !!");
            }
        } else {
            LOG.error("Please create usmUser before add it into SNMP !!");
        }
    }

    /**
     * Create userTarget by those informations from H3CInfos.H3CUserTarget.
     */
    private void createUserTargetByH3CInfos() {
        LOG.info("Creating userTarget...");
        H3CUserTarget userTarget = h3CInfos.getH3CUserTarget();

        String address = userTarget.getAddress();
        String securityName2 = userTarget.getSecurityName2();
        int securityLevel = userTarget.getSecurityLevel();
        int securityModel = userTarget.getSecurityModel();
        int retries = userTarget.getRetries();
        long timeout = userTarget.getTimeout();
        int version = userTarget.getVersion();

        createUserTarget(address, securityName2, securityLevel, securityModel,
                /*int maxSizeRequestPDU,*/ retries, timeout, version);
    }

    /**
     * @param address
     * @param securityName  == userName[addUserIntoSNMP] == securityName[createUser]
     * @param securityLevel [NOAUTH_NOPRIV = 1  |  AUTH_NOPRIV = 2  |  AUTH_PRIV = 3] in SecurityLevel
     * @param securityModel 3 (The H3CUserTarget target can only be used with the User Based Security Model (USM))
     *                      //     * @param maxSizeRequestPDU The minimum PDU length is: 484; default: '\uffff'
     * @param version       default: 3
     */
    private void createUserTarget(String address, String securityName, int securityLevel, int securityModel,
                                  /*int maxSizeRequestPDU,*/ int retries, long timeout, int version) {
        userTarget = new UserTarget();
        userTarget.setAddress(GenericAddress.parse("udp:" + address + "/161"));
        userTarget.setSecurityName(new OctetString(securityName));
        userTarget.setSecurityLevel(securityLevel);
        userTarget.setSecurityModel(securityModel);
        userTarget.setRetries(retries);
        userTarget.setTimeout(timeout);
        userTarget.setVersion(version);
    }

    private void addOIDs2PDU(List<String> oidList) {
        pdu = new ScopedPDU();
        pdu.setType(PDU.GET);
        for (String oid : oidList) {
            pdu.add(new VariableBinding(new OID(oid)));
        }
    }

    /**
     * Send PDU to H3C, then waiting response event will be caught by listener.
     *
     * @param snmp
     * @param userTarget
     * @param pdu
     * @throws IOException
     * @throws InterruptedException
     */
    private void sendGetPDU(Snmp snmp, UserTarget userTarget, ScopedPDU pdu)
            throws IOException, InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);
        ResponseListener listener = new ResponseListener() {

            @Override
            public void onResponse(ResponseEvent event) {
                ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                PDU response = event.getResponse();
                PDU request = event.getRequest();
                LOG.debug("[request]: {}", request);

                if (response == null) {
                    LOG.error("[ERROR]: response is null");

                } else if (response.getErrorStatus() != 0) {
                    LOG.error("[ERROR]: response status {}, Text: {}",
                            response.getErrorStatus(),
                            response.getErrorStatusText());
                } else {
                    LOG.debug("Received response Success!");
                    for (int i = 0; i < response.size(); i++) {
                        VariableBinding vb = response.get(i);
                        LOG.info("{} = {}",
                                vb.getOid(),
                                vb.getVariable());
                    }
                    LOG.debug("SNMP Async GetList OID finished. ");
                    latch.countDown();
                }
            }
        };
        snmp.send(pdu, userTarget, null, listener);
        LOG.debug("async send pdu wait for response...");

        boolean wait = latch.await(3, TimeUnit.SECONDS);
        LOG.debug("latch.await =:" + wait);
    }
}
