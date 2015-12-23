/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.StrategyStatus;
import com.pemass.persist.enumeration.StrategyTypeEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description: 积分红包发行策略实体
 * @Author: he jun cheng
 * @CreateTime: 2014-12-01 11:31
 */
@Entity
@Table(name = "poi_issue_strategy")
public class IssueStrategy extends BaseDomain {

    @Column(name = "strategy_name", nullable = false)
    private String strategyName;    //策略名

    @Column(name = "organization_id")
    private Long organizationId;    //商户下的账户

    @Column(name = "strategy_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private StrategyTypeEnum strategyType;  // 操作类型

    @Column(name = "users", length = 2000)
    private String users;   //user主键的集合，如果该策略是放红包的红包广场则为空

    @Column(name = "strategy_status")
    @Enumerated(EnumType.STRING)
    private StrategyStatus strategyStatus;  //策略的执行状态

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime; // 策略有效期-起始时间

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;   // 策略有效期-结束时间

    @Column(name = "execute_start_time")
    private String executeStartTime;  // 每日有效开始时间

    @Column(name = "execute_end_time")
    private String executeEndTime;    // 每日有效结束时间

    //=================== getter and setter ================

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public StrategyTypeEnum getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(StrategyTypeEnum strategyType) {
        this.strategyType = strategyType;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public StrategyStatus getStrategyStatus() {
        return strategyStatus;
    }

    public void setStrategyStatus(StrategyStatus strategyStatus) {
        this.strategyStatus = strategyStatus;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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
}