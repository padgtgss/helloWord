/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.bas;


import com.pemass.persist.domain.jpa.bas.Resources;

import java.util.List;


/**
 * @Description: LocationService
 * @Author: luoc
 * @CreateTime: 2014-10-31 17:12
 */
public interface ResourcesService {
    /**
     * 保存图片路径信息
     *
     * @param resources
     * @return
     */
    Resources saveResources(Resources resources);

    /**
     * 根据uuid查询对应的图片信息
     *
     * @param targetUUID
     * @return
     */
    List<Resources> getResourcesListByTargetUUID(String targetUUID);

    /**
     * 批量保持图片资源
     *
     * @param resourcesList
     * @return
     */
    Boolean saveBatch(List<Resources> resourcesList);

    /**
     * 批量更新资源
     * <p/>
     * 1.首先删除所有targetUUID的数据
     * <p/>
     * 2.新增resourcesList里面的数据
     *
     * @param resourcesList 资源列表
     * @param targetUUID    目标UUID
     * @return
     */
    Boolean updateBatch(List<Resources> resourcesList, String targetUUID);


    /**
     * 查询首页
     *
     * @return
     */
    List<Resources> selectResourceList(String targetUUID);

    /**
     * 根据id获取资源信息
     *
     * @param resourcesId
     * @return
     */
    Resources getResourcesInfo(Long resourcesId);

    /**
     * 根据id获取资源信息
     *
     * @param resources
     * @return
     */
    Resources updateResourcesInfo(Resources resources);

    /**
     * 修改资源图片
     *
     * @param imageUrls
     * @param targetUUID
     */
    void updateResource(List<String> imageUrls, String targetUUID);

}
