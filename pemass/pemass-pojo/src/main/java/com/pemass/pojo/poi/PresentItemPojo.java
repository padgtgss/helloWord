/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;

import com.pemass.persist.enumeration.PresentItemTypeEnum;
import com.pemass.pojo.ec.OrderTicketPojo;

import java.util.Date;

/**
 * @Description: 红包项
 * @Author: zhou hang
 * @CreateTime: 2014-11-26 19:50
 */
public class PresentItemPojo {

    private Long id;//ID

    private Integer amount; // 单个包含的数量

    private PresentItemTypeEnum presentItemType;    // 红包项类型

    private Integer isGeneral;  //如果红包内容为积分，则表示积分为通用还是定向【0-定向、1-通用】

    private Date expiryTime;    // 红包项内容过期时间

    private OrderTicketPojo orderTicketPojo;    // 电子票

    //====================================getter and setter=============================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public PresentItemTypeEnum getPresentItemType() {
        return presentItemType;
    }

    public void setPresentItemType(PresentItemTypeEnum presentItemType) {
        this.presentItemType = presentItemType;
    }

    public OrderTicketPojo getOrderTicketPojo() {
        return orderTicketPojo;
    }

    public void setOrderTicketPojo(OrderTicketPojo orderTicketPojo) {
        this.orderTicketPojo = orderTicketPojo;
    }

    public Integer getIsGeneral() {
        return isGeneral;
    }

    public void setIsGeneral(Integer isGeneral) {
        this.isGeneral = isGeneral;
    }
}