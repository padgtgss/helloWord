package com.pemass.service.pemass.bas.impl;

import com.google.common.base.Splitter;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.dao.bas.ResourcesDao;
import com.pemass.persist.domain.jpa.bas.Resources;
import com.pemass.persist.enumeration.ResourceType;
import com.pemass.service.pemass.bas.ResourcesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @Description: ResourcesServiceImpl
 * @Author: luoc
 * @CreateTime: 2014-10-30 18:25
 */
@Service
public class ResourcesServiceImpl implements ResourcesService {
    @Resource
    private ResourcesDao resourcesDao;

    /**
     * 保存资源信息
     *
     * @param resources
     * @return
     */
    @Override
    public Resources saveResources(Resources resources) {
        resourcesDao.persist(resources);
        return resources;
    }

    @Override
    public List<Resources> getResourcesListByTargetUUID(String targetUUID) {
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("targetUUID", targetUUID);
        maps.put("available", AvailableEnum.AVAILABLE);
        return resourcesDao.getEntitiesByFieldList(Resources.class, maps, "sequence", BaseDao.OrderBy.ASC);
    }

    @Override
    public Boolean saveBatch(List<Resources> resourcesList) {
        for (Resources resources : resourcesList) {
            this.saveResources(resources);
        }
        return true;
    }

    @Override
    public Boolean updateBatch(List<Resources> resourcesList, String targetUUID) {
        /**删除所有targetUUID的数据*/
        resourcesDao.deleteBatchByUUID(targetUUID);

        /**新增数据*/
        if (resourcesList != null) {
            for (Resources resources : resourcesList) {
                if (resources != null) {
                    resources.setTargetUUID(targetUUID);
                    this.saveResources(resources);
                }
            }
        }

        return true;
    }

    @Override
    public List<Resources> selectResourceList(String targetUUID) {
        return resourcesDao.selectAllEntities(targetUUID);
    }

    /**
     * 根据id获取资源信息
     *
     * @param resourcesId
     * @return
     */
    @Override
    public Resources getResourcesInfo(Long resourcesId) {
        return resourcesDao.getEntityById(Resources.class, resourcesId);
    }

    /**
     * 修改资源信息
     *
     * @param resources
     * @return
     */
    @Override
    public Resources updateResourcesInfo(Resources resources) {
        return resourcesDao.merge(resources);
    }

    @Override
    public void updateResource(List<String> imageUrls, String targetUUID) {
        // 校验参数
        checkNotNull(imageUrls);
        checkNotNull(targetUUID);

        // 删除旧的图片
        resourcesDao.deleteBatchByUUID(targetUUID);

        // 设置新的图片
        if (imageUrls != null && imageUrls.size() > 0) {
            double sequence = 0;
            for (String image : imageUrls) {
                sequence++;
                Resources resources = new Resources();
                resources.setTargetUUID(targetUUID);
                resources.setUrl(image.trim());
                resources.setSequence(sequence++);
                resources.setResourceType(ResourceType.IMAGE);
                resourcesDao.persist(resources);
            }
        }
    }
}
