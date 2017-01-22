package com.yuzhouwan.bigdata.kafka.util;

import com.yuzhouwan.common.util.PropUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：KafkaConnPool Utils Test
 *
 * @author Benedict Jin
 * @since 2016/12/9
 */
public class KafkaConnPoolUtilsTest {

    //    @Test
    public void getConnTest() throws Exception {
        for (int i = 0; i < 2 * Integer.parseInt(PropUtils.getInstance().getProperty("kafka.conn.pool.size")); i++) {
            KafkaConnPoolUtils.getInstance().getConn();
        }
    }

    @Test
    public void indexTest() throws Exception {
        {
            int CONN_INDEX = 0;
            int CONN_IN_POOL = 3;
            long index = (CONN_INDEX %= CONN_IN_POOL);
            CONN_INDEX++;
            assertEquals(1, CONN_INDEX);
            assertEquals(0, index);
        }
        {
            int CONN_INDEX = 0;
            int CONN_IN_POOL = 3;
            long index = (CONN_INDEX += CONN_INDEX %= CONN_IN_POOL);
            assertEquals(0, CONN_INDEX);
            assertEquals(0, index);
        }
    }
}
