package zookeeper;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: Why zk limits myid range in [1, 255]
 *
 * @author jinjy
 * @since 2015/12/1 0001
 */
public class MyidRangeTest {

    @Test
    public void testByteBuffer(){

        byte b[] = new byte[36];
        ByteBuffer responseBuffer = ByteBuffer.wrap(b);
        responseBuffer.clear();     //set position equals 0
        responseBuffer.getInt();
        responseBuffer.putLong(1);
        responseBuffer.putLong(255);
        responseBuffer.putLong(256);

        /**
         *   xid   +  myid(looking)  +      voteId      +    voteZxid
         * 0 0 0 0 + 0 0 0 0 0 0 0 1 + 0 0 0 0 0 0 0 -1 + 0 0 0 0 0 0 1 0 + 0 0 0 0 0 0 0 0
         *
         * There will be [xid + myid + myid + voteId + voteZxid], when during in leading/following.
         */
        for (byte suB : b) {
            System.out.print(suB + " ");
        }
        System.out.println();

        responseBuffer.clear();
        responseBuffer.getInt();
        assertEquals(1, responseBuffer.getLong());
        assertEquals(255, responseBuffer.getLong());
        assertEquals(256, responseBuffer.getLong());
    }

}
