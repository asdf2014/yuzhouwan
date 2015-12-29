package snmp.v3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：snmp.v3
 *
 * @author jinjy
 * @since 2015/12/29 0029
 */
public class SnmpH3C {

    private static final Logger _log = LoggerFactory.getLogger(SnmpH3C.class);

    private TransportMapping transport;
    private Snmp snmp;

    private UsmUser usmUser;

    private UserTarget userTarget;
    private ScopedPDU pdu;

    public void createSNMP() {

        _log.debug("Creating SNMP...");
        try {
            transport = new DefaultUdpTransportMapping();
            transport.listen();
            snmp = new Snmp(transport);

            // SNMP V3
            USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
            SecurityModels.getInstance().addSecurityModel(usm);

            _log.debug("Snmp created.");
            snmp.listen();
            _log.debug("Snmp listening...");
        } catch (IOException e) {
            throw new RuntimeException("Cannot create snmp!!!", e);
        }
    }

    /**
     * @param securityName
     * @param authenticationProtocol   AuthMD5 | AuthSHA
     * @param authenticationPassphrase
     * @param privacyProtocol          PrivDES | PrivAES128 | PrivAES192 | PrivAES256 | Priv3DES | PrivAES
     * @param privacyPassphrase
     * @return
     */
    public void createUser(String securityName,
                           AuthGeneric authenticationProtocol, String authenticationPassphrase,
                           PrivacyProtocol privacyProtocol, String privacyPassphrase) {

        this.usmUser = new UsmUser(new OctetString(securityName),
                authenticationProtocol.getID(), new OctetString(authenticationPassphrase),
                privacyProtocol.getID(), new OctetString(privacyPassphrase));
    }

    /**
     * @param userName
     */
    public void addUserIntoSNMP(String userName) {

        if (usmUser != null) {
            if (snmp != null) {
                snmp.getUSM().addUser(new OctetString(userName), this.usmUser);
            } else {
                _log.error("Please create a instance of SNMP firstly !!");
            }
        } else {
            _log.error("Please create usmUser before add it into SNMP !!");
        }
    }

    /**
     * @param address
     * @param securityName      == userName[addUserIntoSNMP] == securityName[createUser]
     * @param securityLevel     [NOAUTH_NOPRIV = 1  |  AUTH_NOPRIV = 2  |  AUTH_PRIV = 3] in SecurityLevel
     * @param securityModel     3 (The UserTarget target can only be used with the User Based Security Model (USM))
     * @param maxSizeRequestPDU The minimum PDU length is: 484; default: '\uffff'
     * @param version           default: 3
     */
    public void createUserTarget(String address, String securityName, int securityLevel, int securityModel, /*int maxSizeRequestPDU,*/ int retries, long timeout, int version) {
        userTarget = new UserTarget();
        userTarget.setAddress(GenericAddress.parse("udp:" + address + "/161"));
        userTarget.setSecurityName(new OctetString(securityName));
        userTarget.setSecurityLevel(securityLevel);
        userTarget.setSecurityModel(securityModel);
//        userTarget.setMaxSizeRequestPDU(maxSizeRequestPDU);
        userTarget.setRetries(retries);
        userTarget.setTimeout(timeout);
        userTarget.setVersion(version);
    }

    public void addOIDs2PDU(List<String> oidList) {
        pdu = new ScopedPDU();
//        pdu.setNonRepeaters(1);
        pdu.setType(PDU.GET);
//        pdu.setType(PDU.GETBULK);
//        pdu.setType(PDU.GETNEXT);
        for (String oid : oidList) {
            pdu.add(new VariableBinding(new OID(oid)));
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SnmpH3C snmpH3C = new SnmpH3C();
        snmpH3C.createSNMP();
        Snmp snmp = snmpH3C.getSnmp();

        /**
         * snmp-agent target-host trap address udp-domain 192.168.112.155 udp-port 5000 params securityname zyuc
         *
         * snmp-agent usm-user v3 jinjy managev3group simple authentication-mode md5 hellozyuc privacy-mode 3des hellozyuc acl 2222
         */
        String securityName = "jinjy2"; //"managev3group"; //"zyuc"; //"jinjy";
        AuthGeneric authenticationProtocol = new AuthMD5();
        String authenticationPassphrase = "hellozyuc";
        PrivacyProtocol privacyProtocol = new PrivAES128();      //new Priv3DES();
        String privacyPassphrase = "hellozyuc";
        snmpH3C.createUser(securityName, authenticationProtocol, authenticationPassphrase, privacyProtocol, privacyPassphrase);

        String userName = "jinjy2";   //same as securityName ?
        snmpH3C.addUserIntoSNMP(userName);

        String address = "192.168.6.201";
        String securityName2 = "jinjy2"; //"managev3group"; //"zyuc"; //"jinjy";
        int securityLevel = SecurityLevel.AUTH_PRIV;
        int securityModel = 3;
        /*int maxSizeRequestPDU = '\uffff';*/
        int retries = 3;
        long timeout = 1000 * 10;
        int version = SnmpConstants.version3;
        snmpH3C.createUserTarget(address, securityName2, securityLevel, securityModel, /*int maxSizeRequestPDU,*/ retries, timeout, version);

        UserTarget userTarget = snmpH3C.getUserTarget();

        List<String> oidList = new ArrayList<>();
        oidList.add("1.3.6.1.2.1.1.5.0");       //device type: h3c
//        oidList.add("1.3.6");       // example in "http://www.snmp4j.org/doc/org/snmp4j/Snmp.html"
        snmpH3C.addOIDs2PDU(oidList);

        ScopedPDU pdu = snmpH3C.getPdu();

//        sendBlukPDU(snmp, userTarget, pdu);

        sendGetPDU(snmp, userTarget, pdu);

        snmp.close();
    }

    private static void sendBlukPDU(Snmp snmp, UserTarget userTarget, ScopedPDU pdu) throws IOException {
        ResponseEvent response = snmp.send(pdu, userTarget);

        // extract the response PDU (could be null if timed out)
        PDU responsePDU = response.getResponse();
        // extract the address used by the agent to send the response:
        Address peerAddress = response.getPeerAddress();
        _log.info("Response peerAddress: {}", peerAddress.toString());
    }

    private static void sendGetPDU(Snmp snmp, UserTarget userTarget, ScopedPDU pdu) throws IOException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        ResponseListener listener = new ResponseListener() {

            @Override
            public void onResponse(ResponseEvent event) {
                ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                PDU response = event.getResponse();
                PDU request = event.getRequest();
                _log.debug("[request]: {}", request);

                if (response == null) {
                    _log.error("[ERROR]: response is null");

                } else if (response.getErrorStatus() != 0) {
                    _log.error("[ERROR]: response status {}, Text: {}",
                            response.getErrorStatus(),
                            response.getErrorStatusText());
                } else {
                    _log.debug("Received response Success!");
                    for (int i = 0; i < response.size(); i++) {
                        VariableBinding vb = response.get(i);
                        _log.info("{} = {}",
                                vb.getOid(),
                                vb.getVariable());
                    }
                    _log.debug("SNMP Asyn GetList OID finished. ");
                    latch.countDown();
                }
            }
        };

//        snmp.getBulk(pdu, userTarget, null, listener);

        snmp.send(pdu, userTarget, null, listener);
        _log.debug("asyn send pdu wait for response...");

        boolean wait = latch.await(3, TimeUnit.SECONDS);
        _log.debug("latch.await =:" + wait);
    }

    public Snmp getSnmp() {
        return snmp;
    }

    public UserTarget getUserTarget() {
        return userTarget;
    }

    public ScopedPDU getPdu() {
        return pdu;
    }
}
