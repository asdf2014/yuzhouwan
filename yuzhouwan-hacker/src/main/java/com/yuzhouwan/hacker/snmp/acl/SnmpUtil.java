package com.yuzhouwan.hacker.snmp.acl;

import org.apache.log4j.BasicConfigurator;
import org.snmp4j.*;
import org.snmp4j.asn1.BER;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogFactory;
import org.snmp4j.mp.*;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.ThreadPool;

import java.io.IOException;
import java.util.Vector;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：SnmpUtil
 *
 * @author Benedict Jin
 * @since 2015/11/30
 */
public class SnmpUtil extends Thread implements PDUFactory, CommandResponder {

    public static final int DEFAULT = 0;
    public static final int WALK = 1;
    private static Snmp snmp = null;

    static {
        if (System.getProperty("log4j.configuration") == null) {
            BasicConfigurator.configure();
        }
    }

    protected int _operation = DEFAULT;
    private Target target;
    private Address address;
    private OID _authProtocol;
    private OID _privProtocol;
    private OctetString _privPassphrase;
    private OctetString _authPassphrase;
    private OctetString _community = new OctetString("public");
    private OctetString contextEngineID;
    private OctetString contextName = new OctetString();
    private OctetString _securityName = new OctetString();
    private int _numThreads = 1;
    private int _port = 0;
    private ThreadPool _threadPool = null;
    private boolean _isReceiver = false;
    private OctetString _authoritativeEngineID = new OctetString("1234567");
    private TransportMapping _transport = null;
    private TimeTicks _sysUpTime = new TimeTicks(0);
    private OID _trapOID = new OID("1.3.6.1.4.1.2789.2005");
    private int version = 0;
    private int retries = 1;
    private int timeout = 1000;
    private int pduType = 0;
    private Vector vbs = new Vector();

    public SnmpUtil(String host, String varbind, boolean receiver, int type) {
        version = SnmpConstants.version2c;
        _isReceiver = receiver;
        if (type == 2) {
            pduType = PDU.INFORM;
        } else if (type == 1) {
            pduType = PDU.TRAP;
        }
        setPort();
        if (!_isReceiver) {
            init(host, varbind);
        } else {
            initReceiver(host);
        }
    }

    public SnmpUtil(String host, String varbind, String user, String authProtocol,
                    String authPasshrase, String privProtocol, String privPassphrase,
                    boolean receiver, int type) {

        version = SnmpConstants.version3;
        _isReceiver = receiver;
        _privPassphrase = new OctetString(privPassphrase);
        _authPassphrase = new OctetString(authPasshrase);
        _securityName = new OctetString(user);
        if (authProtocol.equals("MD5")) {
            _authProtocol = AuthMD5.ID;
        } else if (authProtocol.equals("SHA")) {
            _authProtocol = AuthSHA.ID;
        }

        if (privProtocol.equals("DES")) {
            _privProtocol = PrivDES.ID;
        } else if ((privProtocol.equals("AES128")) || (privProtocol.equals("AES"))) {
            _privProtocol = PrivAES128.ID;
        } else if (privProtocol.equals("AES192")) {
            _privProtocol = PrivAES192.ID;
        } else if (privProtocol.equals("AES256")) {
            _privProtocol = PrivAES256.ID;
        }
        if (type == 2) {
            pduType = PDU.INFORM;
        } else if (type == 1) {
            pduType = PDU.TRAP;
        }
        setPort();
        if (!_isReceiver) {
            init(host, varbind);
        } else {
            initReceiver(host);
        }
    }

    protected static void printVariableBindings(PDU response) {
        for (int i = 0; i < response.size(); i++) {
            VariableBinding vb = response.get(i);
            System.out.println(vb.toString());
        }
    }

    protected static void printReport(PDU response) {
        if (response.size() < 1) {
            System.out.println("REPORT PDU does not contain a variable binding.");
            return;
        }

        VariableBinding vb = response.get(0);
        OID oid = vb.getOid();
        if (SnmpConstants.usmStatsUnsupportedSecLevels.equals(oid)) {
            System.out.print("REPORT: Unsupported Security Level.");
        } else if (SnmpConstants.usmStatsNotInTimeWindows.equals(oid)) {
            System.out.print("REPORT: Message not within time window.");
        } else if (SnmpConstants.usmStatsUnknownUserNames.equals(oid)) {
            System.out.print("REPORT: Unknown user name.");
        } else if (SnmpConstants.usmStatsUnknownEngineIDs.equals(oid)) {
            System.out.print("REPORT: Unknown engine id.");
        } else if (SnmpConstants.usmStatsWrongDigests.equals(oid)) {
            System.out.print("REPORT: Wrong digest.");
        } else if (SnmpConstants.usmStatsDecryptionErrors.equals(oid)) {
            System.out.print("REPORT: Decryption error.");
        } else if (SnmpConstants.snmpUnknownSecurityModels.equals(oid)) {
            System.out.print("REPORT: Unknown security model.");
        } else if (SnmpConstants.snmpInvalidMsgs.equals(oid)) {
            System.out.print("REPORT: Invalid message.");
        } else if (SnmpConstants.snmpUnknownPDUHandlers.equals(oid)) {
            System.out.print("REPORT: Unknown PDU handler.");
        } else if (SnmpConstants.snmpUnavailableContexts.equals(oid)) {
            System.out.print("REPORT: Unavailable context.");
        } else if (SnmpConstants.snmpUnknownContexts.equals(oid)) {
            System.out.print("REPORT: Unknown context.");
        } else {
            System.out.print("REPORT contains unknown OID ("
                    + oid.toString() + ").");
        }

        System.out.println(" Current counter value is "
                + vb.getVariable().toString() + ".");
    }

    private static PDU walk(Snmp snmp, PDU request, Target target)
            throws IOException {
        request.setNonRepeaters(0);
        OID rootOID = request.get(0).getOid();
        PDU response = null;
        int objects = 0;
        int requests = 0;
        long startTime = System.currentTimeMillis();
        do {
            requests++;
            ResponseEvent responseEvent = SnmpUtil.snmp.send(request, target);
            response = responseEvent.getResponse();
            if (response != null) {
                objects += response.size();
            }
        }
        while (!processWalk(response, request, rootOID));

        System.out.println();
        System.out.println("Total requests sent:    " + requests);
        System.out.println("Total objects received: " + objects);
        System.out.println("Total walk time:        "
                + (System.currentTimeMillis() - startTime) + " milliseconds");
        return response;
    }

    private static boolean processWalk(PDU response, PDU request, OID rootOID) {
        if ((response == null) || (response.getErrorStatus() != 0)
                || (response.getType() == PDU.REPORT)) {
            return true;
        }
        boolean finished = false;
        OID lastOID = request.get(0).getOid();
        for (int i = 0; (!finished) && (i < response.size()); i++) {
            VariableBinding vb = response.get(i);
            if ((vb.getOid() == null) || (vb.getOid().size() < rootOID.size())
                    || (rootOID.leftMostCompare(rootOID.size(), vb.getOid()) != 0)) {
                finished = true;
            } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
                System.out.println(vb.toString());
                finished = true;
            } else if (vb.getOid().compareTo(lastOID) <= 0) {
                System.out.println("Variable received is not lexicographic successor of requested one:");
                System.out.println(vb.toString() + " <= " + lastOID);
                finished = true;
            } else {
                System.out.println(vb.toString());
                lastOID = vb.getOid();
            }
        }
        if (response.size() == 0) {
            finished = true;
        }
        if (!finished) {
            VariableBinding next = response.get(response.size() - 1);
            next.setVariable(new Null());
            request.set(0, next);
            request.setRequestID(new Integer32(0));
        }
        return finished;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getOperation() {
        return _operation;
    }

    public void setOperation(int operation) {
        _operation = operation;
        if (_operation == WALK) {
            pduType = PDU.GETNEXT;
        }
    }

    public int getPduType() {
        return pduType;
    }

    public void setPort() {
        if (pduType == PDU.INFORM || pduType == PDU.TRAP
                || _isReceiver) {
            _port = 162;
        } else {
            _port = 161;
        }
    }

    public void init(String host, String varBind) {
        vbs = getVariableBinding(varBind);
        if (pduType == PDU.INFORM || pduType == PDU.TRAP) {
            checkTrapVariables(vbs);
        }
        address = new UdpAddress(host + "/" + _port);
        LogFactory.setLogFactory(new Log4jLogFactory());
        BER.setCheckSequenceLength(false);
    }

    public Vector getVariableBindings() {
        return vbs;
    }

    private void addUsmUser(Snmp snmp) {
        snmp.getUSM().addUser(_securityName, new UsmUser(_securityName,
                _authProtocol,
                _authPassphrase,
                _privProtocol,
                _privPassphrase));
    }

    private Snmp createSnmpSession() throws IOException {
        AbstractTransportMapping transport;
        if (address instanceof TcpAddress) {
            transport = new DefaultTcpTransportMapping();
        } else {
            transport = new DefaultUdpTransportMapping();
        }
        // Could save some CPU cycles:
        // transport.setAsyncMsgProcessingSupported(false);
        Snmp snmp = new Snmp(transport);

        if (version == SnmpConstants.version3) {
            USM usm = new USM(SecurityProtocols.getInstance(),
                    new OctetString(MPv3.createLocalEngineID()), 0);
            SecurityModels.getInstance().addSecurityModel(usm);
            addUsmUser(snmp);
        }
        return snmp;
    }

    private Target createTarget() {
        if (version == SnmpConstants.version3) {
            UserTarget target = new UserTarget();
            if (_authPassphrase != null) {
                if (_privPassphrase != null) {
                    target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
                } else {
                    target.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);
                }
            } else {
                target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
            }
            target.setSecurityName(_securityName);
            return target;
        } else {
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(_community);
            return target;
        }
    }

    public PDU send() throws IOException {
        snmp = createSnmpSession();
        this.target = createTarget();
        target.setVersion(version);
        target.setAddress(address);
        target.setRetries(retries);
        target.setTimeout(timeout);
        snmp.listen();

        PDU request = createPDU(target);
        for (Object vb : vbs) {
            request.add((VariableBinding) vb);
        }

        PDU response = null;
        if (_operation == WALK) {
            response = walk(snmp, request, target);
        } else {
            ResponseEvent responseEvent;
            long startTime = System.currentTimeMillis();
            responseEvent = snmp.send(request, target);
            if (responseEvent != null) {
                response = responseEvent.getResponse();
                System.out.println("Received response after "
                        + (System.currentTimeMillis() - startTime) + " millis");
            }
        }
        snmp.close();
        return response;
    }

    public PDU createPDU(Target target) {
        PDU request;
        if (this.target.getVersion() == SnmpConstants.version3) {
            request = new ScopedPDU();
            ScopedPDU scopedPDU = (ScopedPDU) request;
            if (contextEngineID != null) {
                scopedPDU.setContextEngineID(contextEngineID);
            }
            if (contextName != null) {
                scopedPDU.setContextName(contextName);
            }
        } else {
            request = new PDU();
        }
        request.setType(pduType);
        return request;
    }

    private Vector getVariableBinding(String varBind) {
        Vector v = new Vector(varBind.length());
        String oid = null;
        char type = 'i';
        String value = null;
        int equal = varBind.indexOf("={");
        if (equal > 0) {
            oid = varBind.substring(0, equal);
            type = varBind.charAt(equal + 2);
            value = varBind.substring(varBind.indexOf('}') + 1);
        } else {
            v.add(new VariableBinding(new OID(varBind)));
            return v;
        }

        VariableBinding vb = new VariableBinding(new OID(oid));
        if (value != null) {
            Variable variable;
            switch (type) {
                case 'i':
                    variable = new Integer32(Integer.parseInt(value));
                    break;
                case 'u':
                    variable = new UnsignedInteger32(Long.parseLong(value));
                    break;
                case 's':
                    variable = new OctetString(value);
                    break;
                case 'x':
                    variable = OctetString.fromString(value, ':', 16);
                    break;
                case 'd':
                    variable = OctetString.fromString(value, '.', 10);
                    break;
                case 'b':
                    variable = OctetString.fromString(value, ' ', 2);
                    break;
                case 'n':
                    variable = new Null();
                    break;
                case 'o':
                    variable = new OID(value);
                    break;
                case 't':
                    variable = new TimeTicks(Long.parseLong(value));
                    break;
                case 'a':
                    variable = new IpAddress(value);
                    break;
                default:
                    throw new IllegalArgumentException("Variable type " + type + " not supported");
            }
            vb.setVariable(variable);
        }
        v.add(vb);
        return v;
    }

    public void initReceiver(String host) {
        Address address = new UdpAddress(host + "/" + _port);
        try {
            _transport = new DefaultUdpTransportMapping((UdpAddress) address);
        } catch (IOException e) {
            System.out.println("Unable to bind to local IP and port: " + e);
            System.exit(-1);
        }

        _threadPool = ThreadPool.create(this.getClass().getName(), _numThreads);

        MessageDispatcher mtDispatcher =
                new MultiThreadedMessageDispatcher(_threadPool, new MessageDispatcherImpl());

        // add message processing models
        mtDispatcher.addMessageProcessingModel(new MPv1());
        mtDispatcher.addMessageProcessingModel(new MPv2c());

        // add all security protocols
        SecurityProtocols.getInstance().addDefaultProtocols();

        snmp = new Snmp(mtDispatcher, _transport);
        if (snmp != null) {
            snmp.addCommandResponder(this);
        } else {
            System.out.println("Unable to create Target object");
            System.exit(-1);
        }

        if (version == SnmpConstants.version3) {
            mtDispatcher.addMessageProcessingModel(new MPv3());
            MPv3 mpv3 = (MPv3) snmp.getMessageProcessingModel(MessageProcessingModel.MPv3);

            USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(mpv3.createLocalEngineID()), 0);

            SecurityModels.getInstance().addSecurityModel(usm);
            if (_authoritativeEngineID != null) {
                snmp.setLocalEngine(_authoritativeEngineID.getValue(), 0, 0);
            }
            this.addUsmUser(snmp);
        }
    }

    public synchronized void listen() {
        try {
            _transport.listen();
        } catch (IOException e) {
            System.out.println("Unable to listen: " + e);
            System.exit(-1);
        }
        System.out.println("Waiting for traps..");
        try {
            // Wait for traps to come in.
            this.wait();
        } catch (InterruptedException ex) {
            System.out.println("Interrupted while waiting for traps: " + ex);
            System.exit(-1);
        }
    }

    public synchronized void processPdu(CommandResponderEvent e) {
        PDU command = e.getPDU();
        if (command != null) {
            System.out.println(command.toString());
            if ((command.getType() != PDU.TRAP)
                    && (command.getType() != PDU.V1TRAP)
                    && (command.getType() != PDU.REPORT)
                    && (command.getType() != PDU.RESPONSE)) {
                command.setErrorIndex(0);
                command.setErrorStatus(0);
                command.setType(PDU.RESPONSE);
                StatusInformation statusInformation = new StatusInformation();
                StateReference ref = e.getStateReference();
                try {
                    e.getMessageDispatcher().returnResponsePdu(e.
                                    getMessageProcessingModel(),
                            e.getSecurityModel(),
                            e.getSecurityName(),
                            e.getSecurityLevel(),
                            command,
                            e.getMaxSizeResponsePDU(),
                            ref,
                            statusInformation);
                } catch (MessageException ex) {
                    System.err.println("Error while sending response: " + ex.getMessage());
                }
            }
        }
    }

    public void sendAndProcessResponse() {
        try {
            PDU response = this.send();
            if ((getPduType() == PDU.TRAP)
                    || (getPduType() == PDU.REPORT)
                    || (getPduType() == PDU.V1TRAP)
                    || (getPduType() == PDU.RESPONSE)) {
                System.out.println(PDU.getTypeString(getPduType()) + " sent successfully");
            } else if (response == null) {
                System.out.println("Request timed out.");
            } else if (response.getType() == PDU.REPORT) {
                printReport(response);
            } else if (getOperation() == WALK) {
                System.out.println("End of walked subtree '"
                        + ((VariableBinding) getVariableBindings().get(0)).getOid()
                        + "' reached at:");
                printVariableBindings(response);
            } else {
                System.out.println("Received something strange: requestID="
                        + response.getRequestID()
                        + ", errorIndex="
                        + response.getErrorIndex() + ", "
                        + "errorStatus=" + response.getErrorStatusText()
                        + "(" + response.getErrorStatus() + ")");
                printVariableBindings(response);
            }
        } catch (IOException ex) {
            System.err.println("Error while trying to send request: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void checkTrapVariables(Vector vbs) {
        if ((pduType == PDU.INFORM) || (pduType == PDU.TRAP)) {
            if ((vbs.size() == 0)
                    || ((vbs.size() > 1)
                    && (!((VariableBinding) vbs.get(0)).getOid().equals(SnmpConstants.sysUpTime)))) {
                vbs.add(0, new VariableBinding(SnmpConstants.sysUpTime, _sysUpTime));
            }
            if ((vbs.size() == 1) || ((vbs.size() > 2)
                    && (!((VariableBinding) vbs.get(1)).getOid().equals(SnmpConstants.snmpTrapOID)))) {
                vbs.add(1, new VariableBinding(SnmpConstants.snmpTrapOID, _trapOID));
            }
        }
    }
}
