package com.yuzhouwan.site.service.validation;

import com.yuzhouwan.site.api.validation.model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Validator Controller Tester
 *
 * @author Benedict Jin
 * @since 2016/9/6
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "site/src/main/resources")
@ContextConfiguration(locations = "classpath:/service/spring.service.xml")
public class ValidatorControllerTest {

    private static final String ERROR_ID = "{id.empty}";
    private static final String ERROR_CONTENT = "{content.empty}";
    private static final String ERROR_USER_NAME = "{username.empty}";

    private static final String ERROR_PASSWORD_EMPTY = "{password.empty.error}";
    private static final String ERROR_MIDDLE_NAME_EMPTY = "{middleName.may.be.empty}";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void before() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void save() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setUserName("yuzhouwan");
        userModel.setContent(null);
        String response = mockMvc.perform(post("/validator/save").param(/*JSON.toJSONString(userModel)*/"id", "0x5555"))
                .andReturn().getResponse().getContentAsString();
        assertEquals(true, response.contains(ERROR_CONTENT));
        assertEquals(true, response.contains(ERROR_USER_NAME));
    }

    @Test
    public void update() throws Exception {
        String response = mockMvc.perform(post("/validator/update").param("userName", "yuzhouwan"))
                .andReturn().getResponse().getContentAsString();
        assertEquals(false, response.contains(ERROR_ID));
        assertEquals(false, response.contains(ERROR_USER_NAME));
        assertEquals(true, response.contains(ERROR_CONTENT));
    }

    @Test
    public void order() throws Exception {
        String response = mockMvc.perform(post("/validator/update/order")
                .param("firstName", "Benedict")
                .param("lastName", "Jin")
                .param("country", "Earth"))
                .andReturn().getResponse().getContentAsString();
        assertEquals(true, response.contains(ERROR_PASSWORD_EMPTY));
        assertEquals(true, response.contains(ERROR_MIDDLE_NAME_EMPTY));
    }

    @Test
    public void multi() throws Exception {
        String response = mockMvc.perform(post("/validator/multi")
                .param("firstName", "Benedict")
                .param("lastName", "Jin")
                .param("country", "Earth"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(response);
        assertEquals(true, response.contains(ERROR_PASSWORD_EMPTY));
        assertEquals(true, response.contains(ERROR_MIDDLE_NAME_EMPTY));
    }

    @After
    public void after() throws Exception {
        mockMvc = null;
    }

}
