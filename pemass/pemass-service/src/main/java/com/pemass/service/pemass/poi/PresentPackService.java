package com.pemass.service.pemass.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.poi.PresentPack;
import com.pemass.persist.domain.vo.PresentPackVO;
import com.pemass.persist.enumeration.PresentStatusEnum;

import java.util.List;
import java.util.Map;

/**
 * @Description: PresentPackServiceImpl
 * @Author: he jun cheng
 * @CreateTime: 2014-10-13 10:35
 */
public interface PresentPackService {

    /**
     * 保存PrsentPack
     *
     * @param presentPack
     */
    void savePresentPack(PresentPack presentPack);

    /**
     * 更新PresentPack
     *
     * @param source
     * @return
     */
    PresentPack updatePresentPack(PresentPack source);

    /**
     * 根据ID获取PresentPack
     *
     * @param presentPackId
     * @return
     */
    PresentPack selectPresentPackById(Long presentPackId);

    /**
     * 根据ID逻辑删除PresentPack
     *
     * @param presentPackId
     * @return
     */
    PresentPack deletePresentPackById(Long presentPackId);

    /**
     * 发行红包
     *
     * @param presentPackVO 包红包传递过来数据
     */
    void addPresentPack(PresentPackVO presentPackVO);

    /**
     * 红包发行并未审核的记录
     *
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getPackRecord(Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 红包发行记录
     *
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param domainPage
     * @return
     */
    DomainPage getPackIssueRecord(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, DomainPage domainPage);

    /**
     * 获取满足条件的红包发行记录（包含对该批次红包的一个统计）
     *
     * @param conditions
     * @param fuzzyConditions
     * @param domainPage
     * @return
     */
    DomainPage getPackRecordAndAmount(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage);

    /**
     * 获取当前账户指定类型的积分记录
     *
     * @param conditions
     * @return
     */
    DomainPage getPointPurchases(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 获取当前账户的积分认购记录
     *
     * @param conditions
     * @return
     */
    List getPointPurchases(Map<String, Object> conditions);

    /**
     * 根据红包编号获得该批次红包详情
     *
     * @param pid
     * @return
     */
    PresentPack getPackDetailById(Long pid);

    /**
     * 根据红包批次ID，获取某个批次的红包发放记录
     *
     * @param presentPackId 红包批次ID
     * @param domainPage    分页对象
     * @return
     */
    DomainPage getPresentRecord(Long presentPackId, DomainPage domainPage);

    /**
     * 对某个批次某个状态的红包数进行统计
     *
     * @param presentPackId
     * @param presentStatusEnum
     * @return
     */
    Integer getPresentAmount(Long presentPackId, PresentStatusEnum presentStatusEnum);

    /**
     * 校验包红包时使用的积分是否超过可用积分  (根据积分的ID校验)
     *
     * @param pointPurchaseId 积分的批次ID
     * @param needPoint           需要的积分
     * @return 校验结果
     */
    boolean checkPoint(Long pointPurchaseId, Integer needPoint);

    /**
     * 发行红包前校验是否能够生成满足条件的红包
     *
     * @param presentPackVO 包红包的信息
     * @return 返回的校验结果和Message
     */
    Map<String, Object> checkIssuePack(PresentPackVO presentPackVO);

    /**
     * 校验剩余积分是否足够   (根据积分的批次信息校验)
     *
     * @param purchaseId
     * @param needPointAmount
     * @return
     */
    boolean checkPointPurchase(Long purchaseId, Integer needPointAmount);

    /**
     * 根据id获得一个对象
     *
     * @param id
     * @return
     */
    PresentPack getEntityById(Long id);

    /**
     * 修改一个实体
     *
     * @param presentPack
     * @return
     */
    void updateEntity(PresentPack presentPack);

    /**
     * 查询审核记录
     *
     * @param fuzzyMap  模糊查询的匹配参数
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getAuditRecords(Map<String, Object> fuzzyMap, long pageIndex, long pageSize);

    /**
     * 删除过期红包批次
     *
     * @param presentPackId
     */
    void deletePresentPack(Long presentPackId);

}
