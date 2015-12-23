/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description: 红包广场表
 * @Author: he jun cheng
 * @CreateTime: 2014-12-03 10:36
 */
@Entity
@Table(name = "poi_present_square")
public class PresentSquare extends BaseDomain {

    @Column(name = "issue_strategy_id", nullable = false)
    private Long issueStrategyId;

    @Column(name = "present_id", nullable = false)
    private Long presentId;

    @Column(name = "is_claim", nullable = false)
    private Integer isClaim;  //0 - 未领取，1 - 已领取

    @Column(name = "user_id")
    private Long userId;    //领取用户的ID(空表示未领取)

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime; // 策略有效期-起始时间  继承自IssueStrategy

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;   // 策略有效期-结束时间  继承自IssueStrategy

    @Column(name = "execute_start_time")
    private String executeStartTime;  // 每日有效开始时间  继承自IssueStrategy

    @Column(name = "execute_end_time")
    private String executeEndTime;    // 每日有效结束时间  继承自IssueStrategy

    //=================== getter and setter ================


    public Long getIssueStrategyId() {
        return issueStrategyId;
    }

    public void setIssueStrategyId(Long issueStrategyId) {
        this.issueStrategyId = issueStrategyId;
    }

    public Long getPresentId() {
        return presentId;
    }

    public void setPresentId(Long presentId) {
        this.presentId = presentId;
    }

    public Integer getIsClaim() {
        return isClaim;
    }

    public void setIsClaim(Integer isClaim) {
        this.isClaim = isClaim;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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