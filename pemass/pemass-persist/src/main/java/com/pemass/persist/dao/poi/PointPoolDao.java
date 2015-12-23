/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.poi.PointPool;

import java.util.Map;

/**
 * @Description: PointPoolDao
 * @Author: cassiel.liu
 * @CreateTime: 2015-06-30 16:26
 */
public interface PointPoolDao extends BaseDao {

    void recycle(Long pointPoolId, Integer amount);

    void recycle(Integer amount);

    PointPool getPointPool();

    /**
     * 根据条件分页查询所有积分池信息
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getAllPointPoolByPages(Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);
}
