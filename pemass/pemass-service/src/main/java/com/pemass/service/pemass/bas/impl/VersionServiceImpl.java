/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.bas.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.pemass.common.core.constant.ConfigurationConst;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.sys.Version;
import com.pemass.persist.enumeration.AppTypeEnum;
import com.pemass.persist.enumeration.DeviceTypeEnum;
import com.pemass.persist.enumeration.VersionUpdateTypeEnum;
import com.pemass.service.pemass.bas.VersionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: VersionServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2014-11-24 15:43
 */
@Service
public class VersionServiceImpl implements VersionService {

    private Log logger = LogFactory.getLog(VersionServiceImpl.class);

    @Resource
    private BaseDao jpaBaseDao;

    @Override
    public Version compareWithLatestVersion(Integer currentBuildNumber, AppTypeEnum appType, DeviceTypeEnum deviceType) {
        Map<String, Object> paraMap = Maps.newHashMap();
        paraMap.put("appType", appType);
        paraMap.put("deviceType", deviceType);

        /**依次迭代数据库中所有版本*/
        DomainPage<Version> domainPage = jpaBaseDao.getEntitiesPagesByFieldList(Version.class, paraMap, "buildNumber", BaseDao.OrderBy.DESC, 1, Integer.MAX_VALUE);
        List<Version> versionList = domainPage.getDomains();

        Version resultVersion = versionList.get(0);
         /* 当前版本号为最新时返回更新类型为：无需更新 */
        if (resultVersion.getBuildNumber().equals(currentBuildNumber) || resultVersion.getBuildNumber() < currentBuildNumber) {
            resultVersion.setUpdateType(VersionUpdateTypeEnum.NONE_UPDATE);
            return resultVersion;
        }

        /**
         * 迭代剩余版本号，用以判断是否强制更新或可选更新
         */
        for (Version version : versionList) {
            /* 当迭代到版本号小于等于当前版本时，迭代完成，返回更新类型为：可选更新 */
            if (version.getBuildNumber() <= currentBuildNumber) {
                resultVersion.setUpdateType(VersionUpdateTypeEnum.OPTIONAL_UPDATE);
                break;
            }
            /* 当迭代到有一个强制更新的版本时，迭代完成，返回最新版本，表示需要强制更新 */
            if (version.getUpdateType() == VersionUpdateTypeEnum.ENFORCE_UPDATE) {
                resultVersion.setUpdateType(VersionUpdateTypeEnum.ENFORCE_UPDATE);
                break;
            }
        }

        return resultVersion;
    }

    @Override
    public Version insert(Version version) {
        Preconditions.checkNotNull(version);

        if (version.getDeviceType().equals(DeviceTypeEnum.ANDROID)) {
            this.copyToLatestFolder(version);
            String downloadUrl = this.copyToFolder(version);
            version.setDownloadUrl(downloadUrl);
        }
        /* IOS下载地址直接固定 */
        else {
            version.setDownloadUrl("https://itunes.apple.com/cn/app/ji-fen-tong/id1028961126?mt=8");
        }

        version.setBuildTime(new Date());
        jpaBaseDao.persist(version);
        return version;
    }

    @Override
    public DomainPage<Version> select(long pageIndex, long pageSize) {
        DomainPage<Version> domainPage = jpaBaseDao.getAllEntitiesByPage(Version.class, pageIndex, pageSize);
        return domainPage;
    }

    @Override
    public Version update(Version version) {
        Version storeVersion = jpaBaseDao.getEntityById(Version.class, version.getId());
        MergeUtil.merge(version, storeVersion);
        jpaBaseDao.merge(storeVersion);
        return storeVersion;
    }

    /**
     * 将上传的APK拷贝到各自客户端对应的文件夹下
     *
     * @param version
     */
    private String copyToFolder(Version version) {
        File srcFile = new File(ConfigurationConst.RESOURCE_ROOT_PATH + version.getDownloadUrl());
        /*-- 创建目标文件 --*/
        String destinationFile = "/apk";
        if (version.getAppType().equals(AppTypeEnum.B)) {
            destinationFile += "/cloudpos" + "/" + version.getLatestVersion() + ".apk";
        } else {
            destinationFile += "/cloudmoney" + "/" + version.getLatestVersion() + ".apk";
        }
        File destFile = new File(ConfigurationConst.RESOURCE_ROOT_PATH + destinationFile);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        /*-- 拷贝 --*/
        try {
            Files.copy(srcFile, destFile);
        } catch (IOException e) {
            logger.error(e);
        }
        return destinationFile;
    }

    /**
     * 将上传的APK拷贝到存放最新应用的文件夹下
     *
     * @param version
     */
    private void copyToLatestFolder(Version version) {
        File srcFile = new File(ConfigurationConst.RESOURCE_ROOT_PATH + version.getDownloadUrl());
        /*-- 创建目标文件 --*/
        String destinationFileName = version.getAppType().equals(AppTypeEnum.B) ? "cloudpos.apk" : "cloudmoney.apk";
        File destFile = new File(ConfigurationConst.RESOURCE_ROOT_PATH + "/apk" + "/" + destinationFileName);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
       /*-- 拷贝 --*/
        try {
            Files.copy(srcFile, destFile);
        } catch (IOException e) {
            logger.error(e);
        }

    }
}
