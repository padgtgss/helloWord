/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.biz.ExpresspayCard;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.enumeration.CardVarietyEnum;
import com.pemass.persist.enumeration.LogisticsStatusEnum;
import com.pemass.persist.enumeration.OrderItemStatusEnum;

import java.util.List;

/**
 * @Description: ExpresspayCardService
 * @Author: zhou hang
 * @CreateTime: 2015-04-16 16:41
 */
public interface ExpresspayCardService {
    /**
     * 录入卡片
     *
     * @param terminalUserId 收银员id
     * @param cardNumber     卡片号码
     * @param cardCategory   卡片类型
     * @return
     */
    List<String> add(Long terminalUserId, String[] cardNumber, String[] cardCategory);

    /**
     * 批量录入
     *
     * @param terminalUserId  收银员
     * @param beginNumber     起始卡号
     * @param endNumber       结束卡号
     * @param cardVarietyEnum 卡号类型
     * @return
     */
    List<String> adds(Long terminalUserId, String beginNumber, String endNumber, CardVarietyEnum cardVarietyEnum);

    /**
     * 根据卡号 查询详情
     *
     * @param cardNumber     卡号
     * @param terminalUserId 收银员
     * @return
     */
    ExpresspayCard getById(String cardNumber, Long terminalUserId);

    /**
     * 扫描调拨
     *
     * @param cardNumber 卡号
     * @param organizationId 调拨商家
     * @param terminalUserId 收银员
     * @return
     */
    Boolean updateAllot(String[] cardNumber, Long organizationId, Long terminalUserId);

    /**
     * 批量调拨
     *
     * @param beginNumber    起始卡号
     * @param endNumber      结束卡号
     * @param organizationId 调拨商家
     * @param terminalUserId 收银员
     * @return
     */
    Boolean updateAllots(String beginNumber, String endNumber, Long organizationId, Long terminalUserId);

    /**
     * 查询代售卡数量
     * @param terminalUserId 收银员
     * @param statusEnum 使用类型
     * @param logisticsStatusEnum 物流状态
     * @return
     */
    Long onSaleCount(Long terminalUserId, OrderItemStatusEnum statusEnum,LogisticsStatusEnum logisticsStatusEnum);

    /**
     * 库存管理 查询待售卡详情
     *
     * @param terminalUserId 收银员
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage onsaleSearch(Long terminalUserId, Long pageIndex, Long pageSize);

    /**
     * 库存管理 查询已售卡详情
     *
     * @param terminalUserId 收银员
     * @param cardNumber   卡号
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage soldSearch(Long terminalUserId, String cardNumber, long pageIndex, long pageSize);

    /**
     * 根据批次查询卡号
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
     * 根据id查询对象
     * @param expresspayCardId
     * @return
     */
    ExpresspayCard getById(Long expresspayCardId);

}
