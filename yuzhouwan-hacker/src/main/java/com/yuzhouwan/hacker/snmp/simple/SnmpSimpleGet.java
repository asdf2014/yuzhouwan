package com.yuzhouwan.hacker.snmp.simple;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：SnmpSimpleGet
 *
 * @author Benedict Jin
 * @since 2015/11/12
 */
public class SnmpSimpleGet {

    private static final int DEFAULT_VERSION = SnmpConstants.version2c;
    private static final String DEFAULT_PROTOCOL = "udp";
    private static final int DEFAULT_PORT = 161;
    private static final long DEFAULT_TIMEOUT = 3 * 1000L;
    private static final int DEFAULT_RETRY = 3;

    /**
     * Create communityTarget.
     *
     * @param ip
     * @param community
     * @return CommunityTarget
     */
    private static CommunityTarget createDefault(String ip, String community) {
        Address address = GenericAddress.parse(DEFAULT_PROTOCOL + ":" + ip
                + "/" + DEFAULT_PORT);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(address);
        target.setVersion(DEFAULT_VERSION);
        target.setTimeout(DEFAULT_TIMEOUT); // milliseconds
        target.setRetries(DEFAULT_RETRY);
        return target;
    }

    /**
     * Get informations of equipment synchronously.
     *
     * @param ip
     * @param community
     * @param oids
     */
    public static void snmpGet(String ip, String community, List<String> oids) {

        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();

            PDU pdu = new PDU();
            for (String oid : oids)
                pdu.add(new VariableBinding(new OID(oid)));
            System.out.println("------- Send PDU -------");
            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);
            System.out.println("PeerAddress:" + respEvent.getPeerAddress());
            PDU response = respEvent.getResponse();

            if (response == null) {
                System.out.println("response is null, request time out");
            } else {
                System.out.println("response pdu size is " + response.size());
                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb = response.get(i);
                    System.out.println(vb.getOid() + " = " + vb.getVariable());
                }
            }
            System.out.println("SNMP GET one OID value finished !");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                }
            }
        }
    }

    /**
     * Get informations of equipment asynchronously.
     *
     * @param ip
     * @param community
     * @param oidList
     */
    public static void snmpSyncGetList(String ip, String community,
                                       List<String> oidList) {
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp;
        try {
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();

            PDU pdu = new PDU();
            for (String oid : oidList) {
                pdu.add(new VariableBinding(new OID(oid)));
            }

            final CountDownLatch latch = new CountDownLatch(1);
            ResponseListener listener = new ResponseListener() {

                @Override
                public void onResponse(ResponseEvent event) {
                    ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    PDU response = event.getResponse();
                    PDU request = event.getRequest();
                    System.out.println("[request]:" + request);

                    if (response == null) {
                        System.out.println("[ERROR]: response is null");

                    } else if (response.getErrorStatus() != 0) {
                        System.out.println("[ERROR]: response status"
                                + response.getErrorStatus() + " Text:"
                                + response.getErrorStatusText());

                    } else {
                        System.out.println("Received response Success!");
                        for (int i = 0; i < response.size(); i++) {
                            VariableBinding vb = response.get(i);
                            System.out.println(vb.getOid() + " = "
                                    + vb.getVariable());
                        }

                        System.out.println("SNMP Async GetList OID finished. ");
                        latch.countDown();
                    }
                }
            };

            pdu.setType(PDU.GET);
            snmp.send(pdu, target, null, listener);
            System.out.println("async send pdu wait for response...");

            boolean wait = latch.await(5, TimeUnit.SECONDS);
            System.out.println("latch.await =:" + wait);

            snmp.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
