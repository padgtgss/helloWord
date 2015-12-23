/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.sys;

import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.sys.Version;
import com.pemass.persist.enumeration.AppTypeEnum;
import com.pemass.persist.enumeration.DeviceTypeEnum;
import com.pemass.pojo.sys.VersionPojo;
import com.pemass.service.pemass.bas.VersionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

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

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Object check(Integer buildNumber, AppTypeEnum appType) {
        Version version = versionService.compareWithLatestVersion(buildNumber, appType, DeviceTypeEnum.ANDROID);
        VersionPojo versionPojo = new VersionPojo();
        MergeUtil.merge(version, versionPojo);
        return versionPojo;
    }

}
