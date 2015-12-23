/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.BaseDomain;

import java.util.Date;
import java.util.Map;

/**
 * @Description: DealChargesDao
 * @Author: cassiel.liu
 * @CreateTime: 2014-12-12 09:27
 */
public interface DealChargesDao extends BaseDao {

    /**
     * 查询某个时间段中的数据
     * @param clazz
     * @param startDate  开始时间
     * @param endDate  结束时间
     * @param pageIndex 当前页
     * @param pageSize  页大小
     * @param <T>
     * @return
     */
    <T extends BaseDomain> DomainPage selectAllEntitiesByTimeQuantum(Class<T> clazz, Date startDate, Date endDate,Map<String,Object> fieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 根据条件获取费用
     * @param clazz
     * @param fieldNameValueMap  查询条件
     * @param <T>
     * @return
     */
    <T extends BaseDomain> Float getSumCharge(Class<T> clazz,String sumFieldName,Map<String,Object> fieldNameValueMap);



}
