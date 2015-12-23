package com.pemass.service.pemass.bas;

import com.pemass.persist.domain.jpa.bas.Device;
import com.pemass.persist.enumeration.DeviceTypeEnum;

/**
 * Created by zhou on 2014/11/10.
 */
public interface DeviceService {

    /**
     * 添加设备信息
     *
     * @param device
     */
    Device insert(Device device);

    /**
     * 根据设备类型统计应用下载总量
     * @param deviceType
     * @return
     */
    Long getDownloadAmountByType(DeviceTypeEnum deviceType);

}
