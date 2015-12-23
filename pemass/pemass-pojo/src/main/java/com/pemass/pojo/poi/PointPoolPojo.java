/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;

import com.pemass.persist.enumeration.PointTypeEnum;
/**
 * @Description: PointPool
 * @Author: lin.shi
 * @CreateTime: 2015-07-31 15:59
 */
public class PointPoolPojo {

    private Long id;

    private String issuer;//发行方

    private String issueName;   //发行名称

    private PointTypeEnum pointType;//积分类型

    private Integer amount;//数量


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public PointTypeEnum getPointType() {
        return pointType;
    }

    public void setPointType(PointTypeEnum pointType) {
        this.pointType = pointType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}