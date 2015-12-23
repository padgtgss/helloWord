/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.bas;

import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.bas.Device;
import com.pemass.pojo.bas.DevicePojo;
import com.pemass.service.pemass.bas.DeviceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description: DeviceController
 * @Author: zhou hang
 * @CreateTime: 2014-11-10 11:40
 */
@Controller
@RequestMapping("/device")
public class DeviceController {

    @Resource
    DeviceService deviceService;

    /**
     * 新增设备
     * <p/>
     * 设备唯一标识：设备序号
     * <p/>
     * 当设备序号数据库已有时，则更新数据库
     *
     * @param device
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object add(Device device) {
        device = deviceService.insert(device);
        DevicePojo pojo = new DevicePojo();
        MergeUtil.merge(device, pojo);
        return pojo;
    }

}