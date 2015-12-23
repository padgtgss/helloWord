/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 商户或者公司清分统计
 * @Author: oliver.he
 * @CreateTime: 2015-08-28 16:13
 */
public class SettlementStatisticsVO implements Serializable, Cloneable {

    private static final long serialVersionUID = 9036157612135803208L;

    public enum GroupType {
        COMPANY("公司"),
        ORGANIZATION("商户"),
        JFT("积分总公司通"),
        CHINAPAY("中国银联");

        private String description;

        GroupType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private Long organizationID;    // 商户的ID (也可以是审核后台Company ID)

    private GroupType companyType;    // 企业的类型 (Company或者Organization)

    private String organizationName;    // 商户名 (也可以是审核后台的Company name)

    private Double outgoTotalAmount;    // 商户出账总金额

    private Double incomeTotalAmount;   // 商户入帐总金额

    private Date statisticDate; // 统计日期


    public SettlementStatisticsVO clone() {
        SettlementStatisticsVO settlementStatisticsVO = null;
        try {
            settlementStatisticsVO = (SettlementStatisticsVO) super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("clone happened exception." + this);
        }
        return settlementStatisticsVO;
    }

    // ================= getter and setter =============== \\


    public Long getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(Long organizationID) {
        this.organizationID = organizationID;
    }

    public GroupType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(GroupType companyType) {
        this.companyType = companyType;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Double getOutgoTotalAmount() {
        return outgoTotalAmount;
    }

    public void setOutgoTotalAmount(Double outgoTotalAmount) {
        this.outgoTotalAmount = outgoTotalAmount;
    }

    public Double getIncomeTotalAmount() {
        return incomeTotalAmount;
    }

    public void setIncomeTotalAmount(Double incomeTotalAmount) {
        this.incomeTotalAmount = incomeTotalAmount;
    }

    public Date getStatisticDate() {
        return statisticDate;
    }

    public void setStatisticDate(Date statisticDate) {
        this.statisticDate = statisticDate;
    }
}