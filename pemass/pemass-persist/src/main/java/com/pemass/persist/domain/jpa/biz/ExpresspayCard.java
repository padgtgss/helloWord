/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.enumeration.*;

import javax.persistence.*;
/**
 * @Description: 中银通卡
 * @Author: zhou hang
 * @CreateTime: 2015-04-14 16:12
 */
@Entity
@Table(name = "biz_expresspay_card")
public class ExpresspayCard extends BaseDomain {

    @Column(name = "card_Identifier",length = 50,nullable = false)
    private String cardIdentifier; //批号

    @Column(name = "organization_id",nullable = false)
    private Long organizationId;//所属商户ID

    @Column(name = "user_id")
    private Long userId;//C用户

    @Column(name = "source_id",nullable = false)
    private Long sourceId;//来源商户

    @Column(name = "card_category",nullable = false)
    @Enumerated(EnumType.STRING)
    private CardVarietyEnum cardCategory;//种类  1,磁条卡2，芯片卡

    @Column(name = "card_number",nullable = false,length = 50, unique = true)
    private String cardNumber;//卡号 唯一

    @Column(name = "cost_price")
    private Double costPrice;//成本价

    @Column(name = "use_status",length = 50,nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderItemStatusEnum useStatus;//使用状态 :已使用  未使用

    @Column(name = "shipping_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private LogisticsStatusEnum shippingStatus;//物流状态,0，入库 1，调拨

    @Column(name = "putaway_style",nullable = false)
    @Enumerated(EnumType.STRING)
    private PutawayStyleEnum putawayStyleEnum;//入库类型,0，批量 1，扫描

    //====================================getter setter================================


    public PutawayStyleEnum getPutawayStyleEnum() {
        return putawayStyleEnum;
    }

    public void setPutawayStyleEnum(PutawayStyleEnum putawayStyleEnum) {
        this.putawayStyleEnum = putawayStyleEnum;
    }

    public String getCardIdentifier() {
        return cardIdentifier;
    }

    public void setCardIdentifier(String cardIdentifier) {
        this.cardIdentifier = cardIdentifier;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public CardVarietyEnum getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(CardVarietyEnum cardCategory) {
        this.cardCategory = cardCategory;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public OrderItemStatusEnum getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(OrderItemStatusEnum useStatus) {
        this.useStatus = useStatus;
    }

    public LogisticsStatusEnum getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(LogisticsStatusEnum shippingStatus) {
        this.shippingStatus = shippingStatus;
    }
}