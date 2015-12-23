/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.StrategyContentTypeEnum;

import javax.persistence.*;

/**
 * @Description: 策略的内容表
 * @Author: he jun cheng
 * @CreateTime: 2015-05-05 14:31
 */
@Entity
@Table(name = "poi_strategy_content")
public class StrategyContent extends BaseDomain {

    @Column(name = "issue_strategy_id")
    private Long issueStrategyId;    // 关联的策略

    @Column(name = "strategy_content_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private StrategyContentTypeEnum strategyContentType;    // 该策略的内容类型 > 可能是红包，可能是积分

    @Column(name = "present_pack_id")
    private Long presentPackId;    // 红包

    @Column(name = "point_purchase_id")
    private Long pointPurchaseId;    // 积分

    @Column(name = "amount", nullable = false)
    private Integer amount;     // 给用户送红包、送积分时 表示单独一个用户的获得数；放红包到广场的时候表示将该批次红包的多少个放到红包广场

    // ===================== getter and setter ==================== \\

    public StrategyContentTypeEnum getStrategyContentType() {
        return strategyContentType;
    }

    public void setStrategyContentType(StrategyContentTypeEnum strategyContentType) {
        this.strategyContentType = strategyContentType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getIssueStrategyId() {
        return issueStrategyId;
    }

    public void setIssueStrategyId(Long issueStrategyId) {
        this.issueStrategyId = issueStrategyId;
    }

    public Long getPresentPackId() {
        return presentPackId;
    }

    public void setPresentPackId(Long presentPackId) {
        this.presentPackId = presentPackId;
    }

    public Long getPointPurchaseId() {
        return pointPurchaseId;
    }

    public void setPointPurchaseId(Long pointPurchaseId) {
        this.pointPurchaseId = pointPurchaseId;
    }
}