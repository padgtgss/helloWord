/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.PresentItemTypeEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description: 红包项
 * @Author: estn.zuo
 * @CreateTime: 2014-12-09 10:42
 */
@Entity
@Table(name = "poi_present_item")
public class PresentItem extends BaseDomain {

    @Column(name = "present_id",nullable = false)
    private Long presentId;    // 红包ID

    @Column(name = "amount")
    private Integer amount; // 单个包含的数量

    @Column(name = "present_item_type", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private PresentItemTypeEnum presentItemType;    // 红包项类型

    @Column(name = "is_general" )
    private Integer isGeneral;  //如果红包内容为积分，则表示积分为通用还是定向【0-定向、1-通用】

    @Column(name = "order_ticket_id")
    private Long orderTicketId;    // 电子票

    @Column(name = "expiry_time", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expiryTime;    // 红包项内容过期时间

    // ======================== getter and setter ======================== \\

    public Long getPresentId() {
        return presentId;
    }

    public void setPresentId(Long presentId) {
        this.presentId = presentId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public PresentItemTypeEnum getPresentItemType() {
        return presentItemType;
    }

    public void setPresentItemType(PresentItemTypeEnum presentItemType) {
        this.presentItemType = presentItemType;
    }

    public Long getOrderTicketId() {
        return orderTicketId;
    }

    public void setOrderTicketId(Long orderTicketId) {
        this.orderTicketId = orderTicketId;
    }

    public Integer getIsGeneral() {
        return isGeneral;
    }

    public void setIsGeneral(Integer isGeneral) {
        this.isGeneral = isGeneral;
    }
}
