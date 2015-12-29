package snmp.v3;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.UserTarget;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：SnmpTelnetH3C Tester
 *
 * @author jinjy
 * @since 2015/12/28 0025
 */
public class SnmpTelnetH3CTest {

    private SnmpTelnetH3C snmpTelnetH3C;

    @Before
    public void before() throws Exception {
        snmpTelnetH3C = new SnmpTelnetH3C();
    }

    @After
    public void after() throws Exception {
        snmpTelnetH3C = null;
    }

    /**
     * Method: getOIDValueV3(OID oid, Address address)
     */
    @Test
    public void testGetOIDValueV3() throws Exception {
        snmpTelnetH3C.showDeviceType();
    }

    @Test
    public void newSnmpV3() throws Exception {

        /**
         * snmp-agent usm-user v3 admin public
         *
         * simple
         *
         * authentication-mode md5 123456789
         * privacy-mode 3des 123456789
         *
         * acl 2222
         */
        String securityName = "admin";

        int v3_ap = 1;
        String authPassphrase = "123456789";

        int v3_privacy = 5;
        String privacyPassphrase = "123456789";

        String ipAddress = "192.168.6.201";
        int securityLevel = 3;
        int retries = 1;
        int timeout = 1000 * 5;

        Snmp snmp = snmpTelnetH3C.createSnmpV3(securityName, v3_ap, authPassphrase, v3_privacy, privacyPassphrase);
        UserTarget userTarget = snmpTelnetH3C.createUserTarget(ipAddress, securityName, securityLevel, retries, timeout);

        snmp.listen();

        PDU response = null;
        //create the pdu
        PDU pdu = new ScopedPDU();
        pdu.setType(PDU.GET);

        Vector _vbs = new Vector();
        VariableBinding vb = new VariableBinding(new OID("1.3.6.1.2.1.1.5.0"));     // device type: h3c
        _vbs.add(vb);

        for (int i = 0; i < _vbs.size(); i++) {
            pdu.add((VariableBinding) _vbs.get(i));
        }
        try {
            /**
             * Not work, response is null :(
             */
            response = snmp.send(pdu, userTarget).getResponse();
            if (response == null)
                System.out.println("Response is null !!");
            else
                System.out.println("list " + response.toString());
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (snmp != null)
                snmp.close();
            response = null;
            userTarget = null;
            pdu = null;
        }

//        getTree(snmp, userTarget);
    }

    private void getTree(Snmp snmp, UserTarget userTarget) {
        PDUFactory pf = new DefaultPDUFactory(PDU.GET);
        TreeUtils treeUtils = new TreeUtils(snmp, pf);
        treeUtils.setIgnoreLexicographicOrder(true);

        List tableEventList = treeUtils.getSubtree(userTarget, new OID("1.3.6.1.2.1.1")/*rootOID[0]*/);

        TreeEvent perTableEvent;
        VariableBinding[] columnValueArray;
        VariableBinding columnValue;
        for (int i = 0; i < tableEventList.size(); i++) {
            Object o = tableEventList.get(i);
            if (o == null) {
                System.out.printf("null !!!");
                continue;
            }
            perTableEvent = (TreeEvent) o;
            columnValueArray = perTableEvent.getVariableBindings();
            if (columnValueArray == null) {
                System.out.println("perTableEvent.getVariableBindings() is null !!!");
                continue;
            }
            for (int j = 0; j < columnValueArray.length; j++) {
                columnValue = columnValueArray[j];
                if (columnValue == null)
                    continue;
                String value = columnValue.toString().substring(
                        columnValue.toString().indexOf("=") + 1,
                        columnValue.toString().length()).trim();
                System.out.println(formatString(value, "utf8"));
            }
        }
    }

    private String formatString(String octetString, String encode) {
        String retString = octetString;
        String[] temps = octetString.split(":");
        if (temps.length > 12) {// 字符串包含的":" 个数大于12，认定此字符串是经过十六进制转化的, 那么进行以下还原操作
            try {
                byte[] bs = new byte[temps.length];
                for (int i = 0; i < temps.length; i++) {
                    bs[i] = (byte) Integer.parseInt(temps[i], 16);
                }
                retString = new String(bs, encode);
            } catch (Exception e) {
                e.printStackTrace();
                return retString;
            }
        }
        return retString.trim();
    }
} 
