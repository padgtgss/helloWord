/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi;


import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.domain.jpa.poi.PointPurchase;
import com.pemass.persist.enumeration.PointTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * @Description: PointDsao
 * @Author: cassiel.liu
 * @CreateTime: 2014-10-20 15:00
 */
public interface PointPurchaseDao {

    /**
     * 根据对象的属性集合查询对象 ,fieldNameValueMap每个属性值采用精确匹配 ,fuzzyFieldNameValueMap模糊匹配
     *
     * @param fieldNameValueMap      精确查询匹配参数
     * @param fuzzyFieldNameValueMap 模糊查询匹配参数
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getEntitiesPages(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 查询审核记录
     *
     * @param fuzzyMap  模糊查询匹配参数
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getAuditRecordByPages(Map<String, Object> fuzzyMap, long pageIndex, long pageSize);

    /**
     * 查询商家所有可用积分
     *
     * @param organizationId 组织机构id
     * @param pointType      积分类型
     * @return
     */
    List<PointPurchase> getJudgeUseableAmountDao(Long organizationId, PointTypeEnum pointType);

    /**
     * 根据“时间区间”分页查询
     *
     * @param clazz
     * @param fieldName “时间字段”
     * @param start     开始时间
     * @param end       结束时间
     * @param filedMap  其他条件集合
     * @param pageIndex 当前页
     * @param pageSize  页大小
     * @return
     */
    DomainPage getEntitiesPagesByTime(Class clazz, String fieldName, Object start, Object end, Map<String, Object> filedMap, long pageIndex, long pageSize);

    /**
     * 查询积分收支明细
     *
     * @param pointType      积分类型
     * @param organizationId 认购机构id
     * @param pageIndex      当前页
     * @param pageSize       页大小
     * @return
     */
    List getPointPaymentDetails(String pointType, Long organizationId, Map<String, Object> fieldMap, long pageIndex, long pageSize);

    /**
     * 积分收支明细的总记录数
     *
     * @param pointType      积分类型
     * @param organizationId 认购机构id
     * @return
     */
    Long getPointPaymentDetailsCount(String pointType, Long organizationId, Map<String, Object> fieldMap);


    /**
     * 查询某一个商户所有积分认购的批次的id
     *
     * @param organizationId
     * @return
     */
    List getPointPurchaseIdByOrganizationId(Long organizationId);

    /**
     * 根据条件获取某一批积分的使用情况
     *
     * @param pointPurchase 认购对象
     * @param pageIndex     当前页
     * @param pageSize      页大小
     * @return
     */
    <T extends BaseDomain> DomainPage getPointUsedDetailsByFiled(Class<T> clazz, PointPurchase pointPurchase, long pageIndex, long pageSize);

    /**
     * 获取全部
     * 有效打积分认购记录
     * 未过期 有效积分大于0 手动认购
     *
     * @param conditions
     * @param domainPage
     * @return
     */
    DomainPage getValidPointPurchase(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 获取全部
     * 有效打积分认购记录
     * 未过期 有效积分大于0 手动认购
     *
     * @param conditions
     * @return
     */
    List<PointPurchase> getValidPointPurchaseList(Map<String, Object> conditions);

    /**
     * 统计当日积分认购的总量
     * @return
     */
    Long getPurchaseAmountOfDayByType(PointTypeEnum pointType);
}
