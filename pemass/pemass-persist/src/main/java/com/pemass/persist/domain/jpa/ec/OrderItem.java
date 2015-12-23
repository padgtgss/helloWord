/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.ec;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Description: 订单项,与订单表进行关联
 * @Author: huang zhuo
 * @CreateTime: 2014-11-14 14:30
 */
@Entity
@Table(name = "ec_order_item")
public class OrderItem extends BaseDomain {

    @Column(name = "order_id")
    private Long orderId;//订单ID

    @Column(name = "order_identifier", length = 50)
    private String orderIdentifier;   //订单编号

    @Column(name = "product_snapshot_id")
    private Long productSnapshotId;    //产品快照ID

    @Column(name = "collect_money_strategy_snapshot_id")
    private Long collectMoneyStrategySnapshotId;  //策略快照ID

    @Column(name = "amount")
    private Integer amount;   //购买数量;

    @Column(name = "price")
    private Double price;   //商品单价;


    //=============getter and setter ===================

    public String getOrderIdentifier() {
        return orderIdentifier;
    }

    public void setOrderIdentifier(String orderIdentifier) {
        this.orderIdentifier = orderIdentifier;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductSnapshotId() {
        return productSnapshotId;
    }

    public void setProductSnapshotId(Long productSnapshotId) {
        this.productSnapshotId = productSnapshotId;
    }

    public Long getCollectMoneyStrategySnapshotId() {
        return collectMoneyStrategySnapshotId;
    }

    public void setCollectMoneyStrategySnapshotId(Long collectMoneyStrategySnapshotId) {
        this.collectMoneyStrategySnapshotId = collectMoneyStrategySnapshotId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

