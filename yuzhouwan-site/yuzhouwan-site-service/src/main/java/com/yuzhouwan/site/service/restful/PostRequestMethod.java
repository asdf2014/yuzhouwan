package com.yuzhouwan.site.service.restful;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Few stuff about POST query in RESTful style
 *
 * @author Benedict Jin
 * @since 2016/3/18
 */
@RestController
@RequestMapping("/post")
public class PostRequestMethod {

    @RequestMapping(value = "/msg", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public String singleIp(String msg) {
        System.out.println("Arrival msg:\t" + msg);
        return "[{\"rule\":\"is down\", \"groupId\":\"1\",\"mode\":\"WECHAT\", \"level\":\"1\"}]";
    }
}
