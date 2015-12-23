/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.ec;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.OrderItemStatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Description:
 * 订单票码表,与订单进行关联
 * <p/>
 * 只有虚拟类别的商品才存在电子票,实体商品不存在
 *
 * @Author:luoc
 * @CreateTime: 2015-06-09 17:20
 */
@Entity
@Table(name = "ec_order_ticket")
public class OrderTicket extends BaseDomain {

    @Column(name = "order_id")
    private Long orderId;   //订单

    @Column(name = "product_snapshot_id")
    private Long productSnapshotId;    //产品快照ID

    @Column(name = "user_id")
    private Long userId;   //购买用户

    @Column(name = "ticket_code", length = 50)
    private String ticketCode;   //票码

    @Column(name = "qr_code", length = 200)
    private String qrCode;   //二维码

    @Column(name = "used_uid")
    private Long usedUserId;   //使用用户

    @Column(name = "ticketer_id")
    private Long ticketerUserId;   //检票员ID

    @Column(name = "use_status")
    @Enumerated(EnumType.STRING)
    private OrderItemStatusEnum useStatus;  //使用状态

    @Column(name = "use_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date useTime;   //使用时间

    @Column(name = "expiry_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryTime;  //过期时间

    @Column(name = "is_shared")
    private Integer isShared; //是否已经分享

    //=============getter and setter ===================

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Long getTicketerUserId() {
        return ticketerUserId;
    }

    public void setTicketerUserId(Long ticketerUserId) {
        this.ticketerUserId = ticketerUserId;
    }

    public OrderItemStatusEnum getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(OrderItemStatusEnum useStatus) {
        this.useStatus = useStatus;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Integer getIsShared() {
        return isShared;
    }

    public void setIsShared(Integer isShared) {
        this.isShared = isShared;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUsedUserId() {
        return usedUserId;
    }

    public void setUsedUserId(Long usedUserId) {
        this.usedUserId = usedUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductSnapshotId() {
        return productSnapshotId;
    }

    public void setProductSnapshotId(Long productSnapshotId) {
        this.productSnapshotId = productSnapshotId;
    }
}

