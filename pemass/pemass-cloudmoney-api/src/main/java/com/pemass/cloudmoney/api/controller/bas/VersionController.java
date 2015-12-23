/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.bas;

import com.google.common.collect.Maps;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.sys.Version;
import com.pemass.persist.enumeration.AppTypeEnum;
import com.pemass.persist.enumeration.ConfigEnum;
import com.pemass.persist.enumeration.DeviceTypeEnum;
import com.pemass.pojo.sys.VersionPojo;
import com.pemass.service.pemass.bas.VersionService;
import com.pemass.service.pemass.sys.ConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description: VersionController
 * @Author: estn.zuo
 * @CreateTime: 2014-11-24 16:26
 */
@Controller
@RequestMapping("version")
public class VersionController {

    @Resource
    private VersionService versionService;

    @Resource
    private ConfigService configService;

    /**
     * 获取最新版本号
     *
     * @param buildNumber
     * @param appType
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Object check(Integer buildNumber, AppTypeEnum appType, DeviceTypeEnum deviceType) {
        if (deviceType == null) {
            deviceType = DeviceTypeEnum.ANDROID;
        }
        Version version = versionService.compareWithLatestVersion(buildNumber, appType, deviceType);
        VersionPojo versionPojo = new VersionPojo();
        MergeUtil.merge(version, versionPojo);
        return versionPojo;
    }

    @RequestMapping(value = "audit", method = RequestMethod.GET)
    @ResponseBody
    public Object audit() {
        Map<String, String> result = Maps.newHashMap();
        result.put("auditStatus", configService.getConfigByKey(ConfigEnum.IOS_AUDIT_STATUS).getValue());
        return result;
    }
}
