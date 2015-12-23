/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.sys;

import com.pemass.persist.enumeration.VersionUpdateTypeEnum;
import com.pemass.pojo.serializer.ResourceUrlSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @Description: VersionPojo
 * @Author: estn.zuo
 * @CreateTime: 2014-11-24 16:31
 */
public class VersionPojo {

    private VersionUpdateTypeEnum updateType;  //更新类型

    private Integer buildNumber;    //构建版本号

    private String latestVersion;   //最新版本号

    private String releaseLog; //版本日志

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String downloadUrl; //下载地址

    public VersionUpdateTypeEnum getUpdateType() {
        return updateType;
    }

    public void setUpdateType(VersionUpdateTypeEnum updateType) {
        this.updateType = updateType;
    }

    public Integer getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(Integer buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getReleaseLog() {
        return releaseLog;
    }

    public void setReleaseLog(String releaseLog) {
        this.releaseLog = releaseLog;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
