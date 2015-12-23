/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;

import java.util.Date;

/**
 * @Description: OnePointDetailPojo
 * @Author: zhou hang
 * @CreateTime: 2015-06-12 14:12
 */
public class OnePointDetailPojo {

    private Long id; //id

    private Date createTime;//入账时间

    private Date expiryTime;//截止时间

    private Integer amount; //还款总额

    private String productName;//商品名称

    private String userName;   //用户名

    private Long uid;

    private Integer useableAmount;  //可用积分数

    private String belongUserName;   //用户名

    public OnePointDetailPojo(){}

    public OnePointDetailPojo(Long uid,String userName, Integer amount, Integer useableAmount, Date createTime) {
        this.userName = userName;
        this.amount = amount;
        this.useableAmount = useableAmount;
        this.createTime = createTime;
        this.uid = uid;
    }


    //========================================getter and setter===============================================


    public String getBelongUserName() {
        return belongUserName;
    }

    public void setBelongUserName(String belongUserName) {
        this.belongUserName = belongUserName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getUseableAmount() {
        return useableAmount;
    }

    public void setUseableAmount(Integer useableAmount) {
        this.useableAmount = useableAmount;
    }
}