/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.biz.ExpresspayCard;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.enumeration.LogisticsStatusEnum;
import com.pemass.persist.enumeration.OrderItemStatusEnum;

import java.util.List;

/**
 * @Description: ExpresspayCardDao
 * @Author: zhou hang
 * @CreateTime: 2015-04-20 16:58
 */
public interface ExpresspayCardDao extends BaseDao {
    /**
     * 根据卡号和所属商户查询
     * @param cardNumber
     * @param organizationId
     * @return
     */
    ExpresspayCard getEntityByid(String cardNumber, Long organizationId);

    /**
     * 批量调拨
     * @param terminalUserId 收银员
     * @param organizationId 所属商户
     * @param beginNumber 起始卡号
     * @param endNumber 结束卡号
     * @param cardIdntifier 批次
     */
    int updateAllots(TerminalUser terminalUserId, Long organizationId, String beginNumber, String endNumber, String cardIdntifier);

    /**
     * 查询代售卡总数
     * @param organizationId 商户
     * @param statusEnum  使用状态
     * @param logisticsStatusEnum 物流状态
     * @return
     */
    Long onSaleCount(Long organizationId, OrderItemStatusEnum statusEnum, LogisticsStatusEnum logisticsStatusEnum);

    /**
     * 根据批次 查询卡号
     * @param identifier
     * @return
     */
    List<Object[]> getBycardIdentifier(String identifier);


    /**
     * 确认入库
     * @param organizationId
     * @param cardIdntifier 批次
     * @return
     */
    boolean updatePutaways(Long organizationId, String cardIdntifier);

    /**
     * 查询已售卡详情
     * @param organizationId
     * @param cardNumber
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getBySoldSearch(Long organizationId, String cardNumber, long pageIndex, long pageSize);
}
