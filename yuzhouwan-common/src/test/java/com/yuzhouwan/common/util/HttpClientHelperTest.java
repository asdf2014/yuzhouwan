package com.yuzhouwan.common.util;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: HttpClientHelper Tester
 *
 * @author Benedict Jin
 * @since 2016/3/21 0030
 */
public class HttpClientHelperTest {

//    @Test
    public void getPlainTest() throws Exception {

        String url = String.format("http://116.8.117.97:8080/netflow_rest/threshold/%s/%s/%s", "113.12.83.4", "10", "5");
        String response = HttpClientHelper.getInstance().getPlain(url, null, null);
        if (StrUtils.isEmpty(response)) {
            System.out.println("empty");
        }
    }
}
