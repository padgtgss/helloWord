/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.bas;

import com.pemass.persist.enumeration.DeviceTypeEnum;

/**
 * @Description: DevicePojo
 * @Author: estn.zuo
 * @CreateTime: 2015-04-14 16:03
 */
public class DevicePojo {

    private Long id;

    private String deviceSerial;    //设备序号，唯一标示一个设备

    private DeviceTypeEnum deviceType;  //设备类型

    private String deviceName;  //设备名称

    private String deviceModel; //设备型号(如：iphone 4s)

    private String systemVersion;   //系统版本号

    private String resolution;  //分辨率

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
