/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.BaseDomain;

import java.util.Date;

/**
 * @Description: DealPointDao
 * @Author: cassiel.liu
 * @CreateTime: 2014-12-12 14:17
 */
public interface DealPointDao extends BaseDao {

    /**
     * 查询某个时间段中的数据
     * @param clazz
     * @param dealType 分成类型【1--积分手续费分成，2--商品服务费分成】
     * @param startDate 开始时间
     * @param endDate  结束时间
     * @param pageIndex  当前页
     * @param pageSize  页大小
     * @param <T>
     * @return
     */
    <T extends BaseDomain> DomainPage selectAllEntitiesByTimeQuantum(Class<T> clazz,Long organizationId,Integer dealType, Date startDate, Date endDate, long pageIndex, long pageSize);




}
