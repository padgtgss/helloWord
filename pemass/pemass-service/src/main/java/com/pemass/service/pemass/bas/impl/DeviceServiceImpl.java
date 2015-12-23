/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.bas.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.bas.DeviceDao;
import com.pemass.persist.domain.jpa.bas.Device;
import com.pemass.persist.enumeration.DeviceTypeEnum;
import com.pemass.service.pemass.bas.DeviceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: DeviceServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2014-11-10 14:03
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private BaseDao jpaBaseDao;
    @Resource
    private DeviceDao deviceDao;

    @Override
    public Device insert(Device device) {
        Device deviceEntity = this.hasBeing(device.getDeviceSerial());
        //判断设备是否存在
        if (deviceEntity != null) {
            MergeUtil.merge(device, deviceEntity);
            jpaBaseDao.merge(deviceEntity);
            return deviceEntity;
        }

        jpaBaseDao.persist(device);
        return device;
    }

    @Override
    public Long getDownloadAmountByType(DeviceTypeEnum deviceType) {
        Preconditions.checkNotNull(deviceType);
        return deviceDao.getDownloadAmountByType(deviceType);
    }


    /**
     * 判断设备是否存在
     *
     * @param deviceSerial
     * @return
     */
    private Device hasBeing(String deviceSerial) {
        Device device = new Device();
        if (StringUtils.isNotBlank(deviceSerial)) {
            device = jpaBaseDao.getEntityByField(Device.class, "deviceSerial", deviceSerial);
        }
        return device;
    }
}