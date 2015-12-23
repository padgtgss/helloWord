/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.sys;

import com.google.common.collect.ImmutableMap;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.enumeration.ConfigEnum;
import com.pemass.service.pemass.sys.ConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Config
 */
@Controller
@RequestMapping("/config")
public class ConfigController {

    @Resource
    private ConfigService configService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Object getConfig(ConfigEnum configEnum) {
        Config config = configService.getConfigByKey(configEnum);

        return ImmutableMap.of("result", config.getValue());
    }

}