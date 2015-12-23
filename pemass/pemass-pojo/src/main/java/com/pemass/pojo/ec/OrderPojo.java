/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.ec;

import com.pemass.persist.enumeration.OrderStatusEnum;
import com.pemass.persist.enumeration.OrderTypeEnum;
import com.pemass.persist.enumeration.PayStatusEnum;
import com.pemass.pojo.biz.SitePojo;
import com.pemass.pojo.serializer.ResourceUrlSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * @Description: OrderPojo
 * @Author: pokl.huang
 * @CreateTime: 2014-11-18 10:35
 */
public class OrderPojo {

    private Long id;    //订单id

    private String orderIdentifier; //订单编号

    private String username; //购买者

    private OrderTypeEnum orderType;    //订单类型

    private OrderStatusEnum orderStatus; //订单状态

    private PayStatusEnum payStatus;    //支付状态

    private Double originalPrice;        //原价

    private Integer amount;     //商品数量

    private Double totalPrice;   //实际支付金额(订单总金额 = totalPrice + totalPointE)

    private Double cashbackPrice;   //返现金额

    private Double loanRate;//贷款利率

    private Integer totalPointP;    //消耗E积分数量

    private Integer totalPointE;    //消耗E通币数量

    private Integer totalPointO;    //消耗一元购积分数

    private Integer givingPointP;  //赠送E积分数量

    private Integer givingPointE;  //赠送E通币数量

    private Integer givingPointO;  //赠送一元购积分数量

    private InvoicePojo invoice;    //发票

    private Integer isReturn;   //是否支持退货【0-不退、1-可退】

    private Date orderTime;   //下单时间

    private Date payTime;   //支付时间

    private Date unsubscribeTime;   //取消时间

    private String remark; //订单备注

    private String externalOrderIdentifier;   //外部订单号

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String barCode;   //条形码,二维码由客户端根据订单编号自动生成

    private List<OrderItemPojo> orderItemPojoList;  //订单项列表

    private List<OrderTicketPojo> orderTicketPojoList;  //电子票列表

    private SitePojo sitePojo; //营业点【提货地点】

    //============================getter and setter====================================


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderIdentifier() {
        return orderIdentifier;
    }

    public void setOrderIdentifier(String orderIdentifier) {
        this.orderIdentifier = orderIdentifier;
    }

    public OrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PayStatusEnum getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatusEnum payStatus) {
        this.payStatus = payStatus;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getTotalPointP() {
        return totalPointP;
    }

    public void setTotalPointP(Integer totalPointP) {
        this.totalPointP = totalPointP;
    }

    public Integer getTotalPointE() {
        return totalPointE;
    }

    public void setTotalPointE(Integer totalPointE) {
        this.totalPointE = totalPointE;
    }

    public Integer getTotalPointO() {
        return totalPointO;
    }

    public void setTotalPointO(Integer totalPointO) {
        this.totalPointO = totalPointO;
    }

    public Integer getGivingPointP() {
        return givingPointP;
    }

    public void setGivingPointP(Integer givingPointP) {
        this.givingPointP = givingPointP;
    }

    public Integer getGivingPointE() {
        return givingPointE;
    }

    public void setGivingPointE(Integer givingPointE) {
        this.givingPointE = givingPointE;
    }

    public Integer getGivingPointO() {
        return givingPointO;
    }

    public void setGivingPointO(Integer givingPointO) {
        this.givingPointO = givingPointO;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public InvoicePojo getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoicePojo invoice) {
        this.invoice = invoice;
    }

    public Integer getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(Integer isReturn) {
        this.isReturn = isReturn;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public List<OrderItemPojo> getOrderItemPojoList() {
        return orderItemPojoList;
    }

    public void setOrderItemPojoList(List<OrderItemPojo> orderItemPojoList) {
        this.orderItemPojoList = orderItemPojoList;
    }

    public List<OrderTicketPojo> getOrderTicketPojoList() {
        return orderTicketPojoList;
    }

    public void setOrderTicketPojoList(List<OrderTicketPojo> orderTicketPojoList) {
        this.orderTicketPojoList = orderTicketPojoList;
    }

    public SitePojo getSitePojo() {
        return sitePojo;
    }

    public void setSitePojo(SitePojo sitePojo) {
        this.sitePojo = sitePojo;
    }

    public OrderTypeEnum getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderTypeEnum orderType) {
        this.orderType = orderType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExternalOrderIdentifier() {
        return externalOrderIdentifier;
    }

    public void setExternalOrderIdentifier(String externalOrderIdentifier) {
        this.externalOrderIdentifier = externalOrderIdentifier;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getUnsubscribeTime() {
        return unsubscribeTime;
    }

    public void setUnsubscribeTime(Date unsubscribeTime) {
        this.unsubscribeTime = unsubscribeTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getCashbackPrice() {
        return cashbackPrice;
    }

    public void setCashbackPrice(Double cashbackPrice) {
        this.cashbackPrice = cashbackPrice;
    }

    public Double getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(Double loanRate) {
        this.loanRate = loanRate;
    }
}

