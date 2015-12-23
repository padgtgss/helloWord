/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Description: 自定义收款策略快照表
 * @Author: estn.zuo
 * @CreateTime: 2015-08-03 15:57
 */
@Entity
@Table(name = "poi_collect_money_strategy_snapshot")
public class CollectMoneyStrategySnapshot extends BaseDomain {

    @Column(name = "collect_money_strategy_id")
    private Long collectMoneyStrategyId;    //原始策略ID

    @Column(name = "organization_id")
    private Long organizationId;    //该策略的商家

    @Column(name = "organization_name", length = 50)
    private String organizationName;  //组织名称

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
    private Double discount; //收款折扣

    @Column(name = "giving_ponit_p_desc")
    private String givingPonitPDesc; //赠送E积分描述信息

    @Column(name = "giving_ponit_e_desc")
    private String givingPonitEDesc; //赠送E通币描述信息


    public Long getCollectMoneyStrategyId() {
        return collectMoneyStrategyId;
    }

    public void setCollectMoneyStrategyId(Long collectMoneyStrategyId) {
        this.collectMoneyStrategyId = collectMoneyStrategyId;
    }

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

    public String getGivingPonitPDesc() {
        return givingPonitPDesc;
    }

    public void setGivingPonitPDesc(String givingPonitPDesc) {
        this.givingPonitPDesc = givingPonitPDesc;
    }

    public String getGivingPonitEDesc() {
        return givingPonitEDesc;
    }

    public void setGivingPonitEDesc(String givingPonitEDesc) {
        this.givingPonitEDesc = givingPonitEDesc;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
