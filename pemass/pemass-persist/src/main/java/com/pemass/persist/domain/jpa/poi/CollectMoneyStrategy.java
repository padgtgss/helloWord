/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description: 收款策略
 * @Author: he jun cheng
 * @CreateTime: 2014-12-24 15:30
 */
@Entity
@Table(name = "poi_collect_money_strategy")
public class CollectMoneyStrategy extends BaseDomain {

    @Column(name = "organization_id")
    private Long organizationId;    //该策略的商家

    @Column(name = "strategy_name", nullable = false)
    private String strategyName;    //策略名

    @Column(name = "start_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime; //策略有效期-起始时间

    @Column(name = "end_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;   //策略有效期-结束时间

    @Column(name = "execute_start_time")
    private String executeStartTime;  // 每日有效开始时间

    @Column(name = "execute_end_time")
    private String executeEndTime;    // 每日有效结束时间

    @Column(name = "is_discount", nullable = false)
    private Integer isDiscount; //是否进行收款折扣【0-不折扣、1-折扣】

    @Column(name = "discount")
    private Double discount;    //收款折扣

    @Column(name = "is_deduction_point_p", nullable = false)
    private Integer isDeductionPointP; //是否进行E积分抵扣【0-不抵扣、1-抵扣】

    @Column(name = "total_point_p")
    private Integer totalPointP;  //抵扣E积分数

    @Column(name = "is_deduction_point_e", nullable = false)
    private Integer isDeductionPointE; //是否进行E通币抵扣【0-不抵扣、1-抵扣】

    @Column(name = "total_point_e")
    private Integer totalPointE;   //E通币抵扣数

    @Column(name = "is_giving_point_p", nullable = false)
    private Integer isGivingPointP; //是否赠送E积分【0-不赠送、1-赠送】

    @Column(name = "point_p_scheme_id")
    private Long pointPSchemeId;   //该商品赠送E积分所使用的方案

    @Column(name = "is_giving_point_e", nullable = false)
    private Integer isGivingPointE; //是否赠送E通币【0-不赠送、1-赠送】

    @Column(name = "point_e_scheme_id")
    private Long pointESchemeId;   //该商品赠送贝积分所使用的方案

    @Column(name = "is_valid", nullable = false)
    private Integer isValid;  //是否有效【0 - 失效、1-有效、2-已过期】

    //=========================== getter and setter ==========================\\

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(Integer isDiscount) {
        this.isDiscount = isDiscount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getIsDeductionPointP() {
        return isDeductionPointP;
    }

    public void setIsDeductionPointP(Integer isDeductionPointP) {
        this.isDeductionPointP = isDeductionPointP;
    }

    public Integer getTotalPointP() {
        return totalPointP;
    }

    public void setTotalPointP(Integer totalPointP) {
        this.totalPointP = totalPointP;
    }

    public Integer getIsDeductionPointE() {
        return isDeductionPointE;
    }

    public void setIsDeductionPointE(Integer isDeductionPointE) {
        this.isDeductionPointE = isDeductionPointE;
    }

    public Integer getTotalPointE() {
        return totalPointE;
    }

    public void setTotalPointE(Integer totalPointE) {
        this.totalPointE = totalPointE;
    }

    public Integer getIsGivingPointP() {
        return isGivingPointP;
    }

    public void setIsGivingPointP(Integer isGivingPointP) {
        this.isGivingPointP = isGivingPointP;
    }

    public Long getPointPSchemeId() {
        return pointPSchemeId;
    }

    public void setPointPSchemeId(Long pointPSchemeId) {
        this.pointPSchemeId = pointPSchemeId;
    }

    public Integer getIsGivingPointE() {
        return isGivingPointE;
    }

    public void setIsGivingPointE(Integer isGivingPointE) {
        this.isGivingPointE = isGivingPointE;
    }

    public Long getPointESchemeId() {
        return pointESchemeId;
    }

    public void setPointESchemeId(Long pointESchemeId) {
        this.pointESchemeId = pointESchemeId;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public String getExecuteStartTime() {
        return executeStartTime;
    }

    public void setExecuteStartTime(String executeStartTime) {
        this.executeStartTime = executeStartTime;
    }

    public String getExecuteEndTime() {
        return executeEndTime;
    }

    public void setExecuteEndTime(String executeEndTime) {
        this.executeEndTime = executeEndTime;
    }
}