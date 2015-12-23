/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.enumeration.CardVarietyEnum;
import com.pemass.persist.enumeration.PutawayStatusEnum;
import com.pemass.persist.enumeration.PutawayStyleEnum;

import javax.persistence.*;

/**
 * @Description: 记录表
 * @Author: zhou hang
 * @CreateTime: 2015-04-23 10:11
 */
@Entity
@Table(name = "biz_expresspay_card_detail")
public class ExpresspayCardDetail extends BaseDomain {

    @Column(name = "card_Identifier", length = 50, nullable = false)
    private String cardIdentifier; //批号

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;//所属商户ID

    @Column(name = "source_id", nullable = false)
    private Long sourceId;//来源商户

    @Column(name = "cashier_user_id",nullable = false)
    private Long cashierUserId;//收银员id

    @Column(name = "card_number", nullable = false, length = 50)
    private String cardNumber;//卡号

    @Column(name = "card_category",nullable = false)
    @Enumerated(EnumType.STRING)
    private CardVarietyEnum cardCategory;//种类  1,磁条卡2，芯片卡

    @Column(name = "putaway_style",nullable = false)
    @Enumerated(EnumType.STRING)
    private PutawayStyleEnum putawayStyleEnum;//入库类型,0，批量 1，扫描

    @Column(name = "putaway_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private PutawayStatusEnum putawayStatusEnum;//入库状态,0，在途 1，入库

    //====================getter and setter================


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getCashierUserId() {
        return cashierUserId;
    }

    public void setCashierUserId(Long cashierUserId) {
        this.cashierUserId = cashierUserId;
    }

    public PutawayStyleEnum getPutawayStyleEnum() {
        return putawayStyleEnum;
    }

    public void setPutawayStyleEnum(PutawayStyleEnum putawayStyleEnum) {
        this.putawayStyleEnum = putawayStyleEnum;
    }

    public PutawayStatusEnum getPutawayStatusEnum() {
        return putawayStatusEnum;
    }

    public void setPutawayStatusEnum(PutawayStatusEnum putawayStatusEnum) {
        this.putawayStatusEnum = putawayStatusEnum;
    }
    public CardVarietyEnum getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(CardVarietyEnum cardCategory) {
        this.cardCategory = cardCategory;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardIdentifier() {
        return cardIdentifier;
    }

    public void setCardIdentifier(String cardIdentifier) {
        this.cardIdentifier = cardIdentifier;
    }
}