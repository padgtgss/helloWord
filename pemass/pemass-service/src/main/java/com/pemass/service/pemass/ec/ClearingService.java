package com.pemass.service.pemass.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.ec.Clearing;
import com.pemass.persist.domain.jpa.ec.Settlement;
import com.pemass.persist.enumeration.SettlementRoleEnum;

import java.util.List;
import java.util.Map;

/**
 * @Description: ClearingService
 * @Author: luoc
 * @CreateTime: 2015-08-18 10:38
 */
public interface ClearingService {

    /**
     * 保存清分记录
     *
     * @param clearing
     */
    void saveClearing(Clearing clearing);

    /**
     * 修改清分记录状态
     *
     * @param clearing
     */
    Clearing updateClearing(Clearing clearing);

    /**
     * 分页查询清分信息
     *
     * @param conditions
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectAllClearingByPage(Map<String, Object> conditions, long pageIndex, long pageSize);

    /**
     * 查询初满足条件的的所有结果
     *
     * @param conditions 查询条件
     * @return 返回结果
     */
    List<Map<String, Object>> selectAllClearing(Map<String, Object> conditions);

    /**
     * 对清分表进行汇总到Settlement
     *
     * @return
     */
    List<Settlement> getCountClearing();

    /**
     * 根据条件查询请清分表
     *
     * @return
     */
    List<Clearing> getClearingGroup(Map<String, Object> fieldNameValueMap);

    /**
     * 根据id查询Clearing的信息
     *
     * @param id id
     * @return
     */
    Clearing getClearingById(Long id);

    /**
     * 根据ClearingId集合分页查询
     *
     * @param ids       id集合
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectClearingInfoByIds(List<Long> ids, long pageIndex, long pageSize);


    /**
     * 根据商户id获取该商户的E积分收益
     *
     * @param organizationId 商户id
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectPointProfitByOrganization(Long organizationId, long pageIndex, long pageSize);

    /**
     * 查询用户清分数据
     *
     * @param map
     * @param organizationId
     * @return
     */
    Map<String, Object> getOrganizationClearList(Map<String, Object> map, Long organizationId, String mark, long pageIndex, long pageSize);

    /**
     * 获取出账方和入账方的名字
     *
     * @param settlementRole         清算角色
     * @param settlementRoleTargetId 结算角色ID
     * @return 返回的结果
     */
    Map<String, Object> getOutgoAndIncomeName(SettlementRoleEnum settlementRole, Long settlementRoleTargetId);

    /**
     * 查询商户分润数据
     * @param map
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getBusinessProfitsList(Map<String,Object> map,long pageIndex,long pageSize);
}
