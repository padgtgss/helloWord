/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.bas;

import com.pemass.persist.domain.jpa.bas.Resources;
import com.pemass.pojo.bas.ResourcesPojo;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.pemass.bas.ResourcesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: LoadingController
 * @Author: estn.zuo
 * @CreateTime: 2015-04-16 16:44
 */
@Controller
@RequestMapping("/loading")
public class LoadingController {

    @Resource
    private ResourcesService resourcesService;

    /**
     * 获取云钱包启动时,加载图片的URL地址
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Object loadingimg() {
        List<Resources> resourcesList = resourcesService.getResourcesListByTargetUUID(PemassConst.LOADING_IMAGE_UUID);
        Resources resources = new Resources();
        if (resourcesList.size() == 1) {
            resources = resourcesList.get(0);
        }

        ResourcesPojo resourcesPojo = new ResourcesPojo();
        resourcesPojo.setUrl(resources.getUrl());
        return resourcesPojo;
    }

}
