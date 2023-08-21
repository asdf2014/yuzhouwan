package com.yuzhouwan.site.service.cache;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Example Controller
 *
 * @author Benedict Jin
 * @since 2016/3/28
 */
@Controller
public class ExampleController {

    private static final Logger LOGGER = Logger.getLogger(ExampleController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    @Cacheable("calculateResult")
    public String calculateResult() {
        LOGGER.debug("Performing expensive calculation...");
        // perform computationally expensive calculation
        return "result";
    }
}
