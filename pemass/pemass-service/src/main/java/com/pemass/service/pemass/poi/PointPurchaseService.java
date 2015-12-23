/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.poi.PointPurchase;
import com.pemass.persist.enumeration.PointTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * @Description: PointPurchaseService
 * @Author: cassiel.liu
 * @CreateTime: 2014-10-13 17:28
 */
public interface PointPurchaseService {

    /**
     * 保存数据
     * @param pointPurchase
     * @return
     */
    PointPurchase insert(PointPurchase pointPurchase);


    /**
     * 根据id修改一个实体
     *
     * @param pointPurchase
     * @return
     */
    void updateEntity(PointPurchase pointPurchase);

    /**
     * 分页查询积分认购记录
     *
     * @param map
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getAllListByPage(Map<String, Object> map, Map<String, Object> fuzzyMap, Long pageIndex, Long pageSize);

    /**
     * 根据认购id，查询认购记录
     *
     * @param pointPurchaseId
     * @return
     */
    PointPurchase getById(Long pointPurchaseId);

    /**
     * 查询审核记录
     *
     * @param fuzzyMap  模糊查询匹配参数
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getAuditRecordsByPage(Map<String, Object> fuzzyMap, long pageIndex, long pageSize);


    /**
     * 根据“时间区间”分页查询
     *
     * @param filedName “时间字段”
     * @param start     开始时间
     * @param end       结束时间
     * @param filedMap  其他条件集合
     * @param pageIndex 当前页
     * @param pageSize  页大小
     * @return
     */
    DomainPage<PointPurchase> getEntitiesByPages(String filedName, Object start, Object end, Map<String, Object> filedMap, long pageIndex, long pageSize);


    /**
     * 查询积分收支明细--可以删除
     *
     * @return
     */
    DomainPage getPointPaymentDetails(String pointType, Long organizationId, Map<String, Object> fieldMap, long pageIndex, long pageSize);

    /**
     * 查询某一个商户所有积分认购的批次的id
     *
     * @param organizationId
     * @return
     */
    List getPointPurchaseIdByOrganizationId(Long organizationId);

    /**
     * 获取某一批积分的使用情况
     *
     * @param pointPurchase
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getPointUsedDetails(PointPurchase pointPurchase, long pageIndex, long pageSize);

    /**
     * 根据条件查询所有认购记录
     *
     * @param map
     * @return
     */
    List<PointPurchase> getPointPurchaseList(Map<String, Object> map);

    /**
     * 获取有效的积分认购纪录
     *
     * @param conditions
     * @param domainPage
     * @return
     */
    DomainPage getValidPointPurchase(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 获取有效的积分认购纪录
     *
     * @param conditions
     * @return
     */
    List<PointPurchase> getValidPointPurchaseList(Map<String, Object> conditions);

    /**
     * 保存认购记录
     * @param pointPurchase
     */
    void savePointPurchase(PointPurchase pointPurchase);

    /**
     * 统计当日积分认购的总量
     * @return
     */
    Long getPurchaseAmountOfDayByType(PointTypeEnum pointType);


}
