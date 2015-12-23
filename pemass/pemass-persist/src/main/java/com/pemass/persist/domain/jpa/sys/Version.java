/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.sys;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.AppTypeEnum;
import com.pemass.persist.enumeration.DeviceTypeEnum;
import com.pemass.persist.enumeration.VersionUpdateTypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Description: 版本管理
 * @Author: estn.zuo
 * @CreateTime: 2014-11-24 15:31
 */
@Entity
@Table(name = "sys_version")
public class Version extends BaseDomain {

    @Column(name = "build_number", nullable = false)
    private Integer buildNumber;    //构建版本号

    @Column(name = "latest_version", length = 10, nullable = false)
    private String latestVersion;   //最新版本号

    @Column(name = "device_type", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceTypeEnum deviceType;  //设备类型

    @Column(name = "app_type", length = 2, nullable = false)
    @Enumerated(EnumType.STRING)
    private AppTypeEnum appType; //客户端类型

    @Column(name = "update_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private VersionUpdateTypeEnum updateType;  //更新类型

    @Column(name = "download_url", length = 200, nullable = false)
    private String downloadUrl; //下载地址

    @Column(name = "build_time", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date buildTime; //构建时间

    @Column(name = "release_log", nullable = false)
    private String releaseLog; //版本更新日志

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public AppTypeEnum getAppType() {
        return appType;
    }

    public void setAppType(AppTypeEnum appType) {
        this.appType = appType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Integer getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(Integer buildNumber) {
        this.buildNumber = buildNumber;
    }

    public Date getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(Date buildTime) {
        this.buildTime = buildTime;
    }

    public VersionUpdateTypeEnum getUpdateType() {
        return updateType;
    }

    public void setUpdateType(VersionUpdateTypeEnum updateType) {
        this.updateType = updateType;
    }

    public String getReleaseLog() {
        return releaseLog;
    }

    public void setReleaseLog(String releaseLog) {
        this.releaseLog = releaseLog;
    }

    public DeviceTypeEnum getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceTypeEnum deviceType) {
        this.deviceType = deviceType;
    }
}
