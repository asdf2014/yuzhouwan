package com.yuzhouwan.site.service.restful;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Copyright @ yuzhouwan.com
 * All right reserved.
 * Function：MultiIPAddresses Tester
 *
 * @author Benedict Jin
 * @since 2015/12/7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "site/src/main/resources")
@ContextConfiguration(locations = "classpath:/service/spring.service.xml")
public class MultiIPAddressesTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void before() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * Method: singleIp(@PathVariable String engineIp)
     */
    @Test
    public void testSingleIp() throws Exception {

        mockMvc.perform(get("/rest/ip/8.8.8.8").accept(MediaType.ALL));
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$engineIp").value(isA(String.class)))
//                .andExpect(jsonPath("$engineIp").value(equalTo("8.8.8.8")));
    }

    /**
     * Method: multiIps(@PathVariable("engineIps") List<String> engineIps)
     */
    @Test
    public void testMultiIps() throws Exception {

        mockMvc.perform(get("/rest/ips/8.8.8.8,8.8.4.4"));
    }
}
