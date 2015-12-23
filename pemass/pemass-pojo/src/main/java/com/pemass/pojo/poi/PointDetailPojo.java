/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;


import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PointChannelEnum;
import com.pemass.persist.enumeration.PointTypeEnum;

import java.util.Date;

/**
 * @Description: PointDetailPojo
 * @Author: zhou hang
 * @CreateTime: 2014-11-26 17:15
 */
public class PointDetailPojo {


    private Long userId;//用户

    private Long pointPurchaseId;//积分认购批号

    private Long organizationId;//账户

    private PointTypeEnum pointType;//积分类型【P-E积分,E-E通币】

    private String area;//区域

    private Integer amount;//数量

    private Integer useableAmount;//可用积分数

    private PointChannelEnum pointChannel; //积分来源

    private String sourceName;//积分来源名称

    private Date expiryTime;  //过期时间

    private Date createTime;  //记录时间

    private ConsumeTypeEnum consumeType;   //积分消耗

    private Integer freeAmount;    //冻结积分

    private PointPoolPojo poolPojo;
    //============getter setter==========

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPointPurchaseId() {
        return pointPurchaseId;
    }

    public void setPointPurchaseId(Long pointPurchaseId) {
        this.pointPurchaseId = pointPurchaseId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public PointTypeEnum getPointType() {
        return pointType;
    }

    public void setPointType(PointTypeEnum pointType) {
        this.pointType = pointType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getUseableAmount() {
        return useableAmount;
    }

    public void setUseableAmount(Integer useableAmount) {
        this.useableAmount = useableAmount;
    }

    public PointChannelEnum getPointChannel() {
        return pointChannel;
    }

    public void setPointChannel(PointChannelEnum pointChannel) {
        this.pointChannel = pointChannel;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public PointPoolPojo getPoolPojo() {
        return poolPojo;
    }

    public void setPoolPojo(PointPoolPojo poolPojo) {
        this.poolPojo = poolPojo;
    }

    public ConsumeTypeEnum getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(ConsumeTypeEnum consumeType) {
        this.consumeType = consumeType;
    }

    public Integer getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(Integer freeAmount) {
        this.freeAmount = freeAmount;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}