package com.yuzhouwan.site.cache;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ExampleController {

    private static final Logger log = Logger.getLogger(ExampleController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    @Cacheable("calculateResult")
    public String calculateResult() {
        log.debug("Performing expensive calculation...");
        // perform computationally expensive calculation
        return "result";
    }
}