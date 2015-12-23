/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.ec;

import com.pemass.pojo.biz.ProductSnapshotPojo;
import com.pemass.pojo.poi.CollectMoneyStrategySnapshotPojo;

/**
 * @Description: OrderItemPojo
 * @Author: zhou hang
 * @CreateTime: 2014-11-18 15:10
 */
public class OrderItemPojo {

    private Integer amount;   //购买数量

    private Double price;   //商品单价

    private CollectMoneyStrategySnapshotPojo collectMoneyStrategySnapshotPojo; //机构

    private ProductSnapshotPojo productSnapshotPojo;    //产品快照

    //===============================getter and setter======================================

    public ProductSnapshotPojo getProductSnapshotPojo() {
        return productSnapshotPojo;
    }

    public void setProductSnapshotPojo(ProductSnapshotPojo productSnapshotPojo) {
        this.productSnapshotPojo = productSnapshotPojo;
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

    public CollectMoneyStrategySnapshotPojo getCollectMoneyStrategySnapshotPojo() {
        return collectMoneyStrategySnapshotPojo;
    }

    public void setCollectMoneyStrategySnapshotPojo(CollectMoneyStrategySnapshotPojo collectMoneyStrategySnapshotPojo) {
        this.collectMoneyStrategySnapshotPojo = collectMoneyStrategySnapshotPojo;
    }
}