package com.yuzhouwan.site.service.restful;

import com.yuzhouwan.common.util.StrUtils;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: RESTful 标准化
 *
 * @author Benedict Jin
 * @since 2016/8/2
 */
@RestController
@RequestMapping("/full")
public class FullRestMethod {

    private ConcurrentHashMap<String, String> chm = new ConcurrentHashMap<>();

    /**
     * GET用来获取资源.
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public String getMsg(String key) {
        String result;
        return StrUtils.isEmpty(key) ? "key can not be empty!"
                : (result = chm.get(key)) == null ? "nothing here" : result;
    }

    /**
     * POST用来新建资源（也可以用于更新资源）.
     *
     * @param key
     * @param value
     * @return
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ResponseBody
    public String postMsg(String key, String value) {
        return save(key, value);
    }

    @RequestMapping(value = "/post/consumes", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String postConsumesMsg(String key, String value) {
        return save(key, value);
    }

    /**
     * PUT用来更新资源.
     *
     * @param key
     * @param value
     * @return
     */
    @RequestMapping(value = "/put", method = RequestMethod.PUT)
    @ResponseBody
    public String putMsg(String key, String value) {
        return save(key, value);
    }

    /**
     * DELETE用来删除资源.
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteMsg(String key) {
        return StrUtils.isEmpty(key) ? "key can not be empty!" : chm.remove(key);
    }

    private String save(String key, String value) {
        return StrUtils.isEmpty(key) && StrUtils.isEmpty(value) ? "k-v can not be empty!" : chm.put(key, value);
    }
}
