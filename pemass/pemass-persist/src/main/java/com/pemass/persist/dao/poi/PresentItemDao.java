package com.pemass.persist.dao.poi;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.enumeration.PresentItemTypeEnum;

/**
 * @Description: PointPoolDaoImpl
 * @Author: cassiel.liu
 * @CreateTime: 2015-09-19 16:27
 */
public interface PresentItemDao extends BaseDao {


    /**
     * 根据红包批次获取红包项的数量
     *
     * @param presentPackId
     * @return
     */
    Integer getPresentItemByPresentPackId(Long presentPackId);

    Integer getTotalPointByPresentPackId(Long presentPackId, PresentItemTypeEnum presentItemType);
}
