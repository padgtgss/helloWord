/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.pojo.ec;

import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.pojo.biz.ProductSnapshotPojo;
import com.pemass.pojo.serializer.ResourceUrlSerializer;
import com.pemass.pojo.sys.OrganizationPojo;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * @Description: OrderTicketPojo
 * @Author: estn.zuo
 * @CreateTime: 2015-07-16 10:09
 */
public class OrderTicketPojo {

    private Long id;//ID

    private String ticketCode;   //票码

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String qrCode;   //二维码

    private OrderItemStatusEnum useStatus;  //使用状态

    private Date useTime;   //使用时间

    private Date expiryTime;  //过期时间

    private Integer isShared; //是否已经分享

    private ProductSnapshotPojo productSnapshotPojo;    //产品快照

    private OrganizationPojo organizationPojo;


    //==============================getter and setter===================================


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductSnapshotPojo getProductSnapshotPojo() {
        return productSnapshotPojo;
    }

    public void setProductSnapshotPojo(ProductSnapshotPojo productSnapshotPojo) {
        this.productSnapshotPojo = productSnapshotPojo;
    }

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

    public OrderItemStatusEnum getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(OrderItemStatusEnum useStatus) {
        this.useStatus = useStatus;
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

    public OrganizationPojo getOrganizationPojo() {
        return organizationPojo;
    }

    public void setOrganizationPojo(OrganizationPojo organizationPojo) {
        this.organizationPojo = organizationPojo;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }
}
