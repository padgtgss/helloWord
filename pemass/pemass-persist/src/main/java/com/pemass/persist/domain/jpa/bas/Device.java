/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.bas;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.DeviceTypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * @Description: Device
 * @Author: estn.zuo
 * @CreateTime: 2014-10-30 14:44
 */
@Entity
@Table(name = "bas_device")
public class Device extends BaseDomain {

    @Column(name = "device_serial", length = 50, nullable = false)
    private String deviceSerial;    //设备序号，唯一标示一个设备

    @Column(name = "device_type", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceTypeEnum deviceType;  //设备类型

    @Column(name = "device_name", length = 50)
    private String deviceName;  //设备名称

    @Column(name = "device_model", length = 50)
    private String deviceModel; //设备型号(如：iphone 4s)

    @Column(name = "system_version", length = 20)
    private String systemVersion;   //系统版本号

    @Column(name = "resolution", length = 20)
    private String resolution;  //分辨率


    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public DeviceTypeEnum getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceTypeEnum deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
