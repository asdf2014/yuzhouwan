package com.yuzhouwan.site.restful;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Copyright @ yuzhouwan.com
 * All right reserved.
 * Function: Input multi ip addresses as param into RESTful API in spring
 *
 * @author asdf2014
 * @since 2015/12/7 0007
 */
@RestController
@RequestMapping("/rest")
public class MultiIPAddresses {

    @RequestMapping(value = "/ip/{singleIp:.+}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void singleIp(@PathVariable("singleIp") String singleIp) {
        System.out.println("Single Ip:\t" + singleIp);
    }

    @RequestMapping(value = "/ips/{multiIps:.+}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void multiIps(@PathVariable("multiIps") String multiIps) {

        /**
         * TODO{asdf2014}: post
         */
        if (!"".equals(multiIps.trim())) {

            String[] ips = multiIps.split(",");
            for (String ip : ips) {
                System.out.println(ip + " ");
            }
        }
    }
}
