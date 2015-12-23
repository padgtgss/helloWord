/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.biz;

import java.util.Date;
import java.util.List;

/**
 * @Description: ExpresspayCardSituationPojo
 * @Author: zhou hang
 * @CreateTime: 2015-04-22 16:08
 */
public class ExpresspayCardSituationPojo {

    private String useStatus;//入库类型 【1-批量录入，2-扫描录入】

    private String organizationName;//公司名称

    private Date updateTime;//时间

    private Long count;//总条数

    private String diskCard;//芯片卡

    private String chipCard;//磁条卡

    private List<ExpresspayCardPojo> diskCardPojoList;//扫描芯片卡集合

    private List<ExpresspayCardPojo> chipCardPojoList;//扫描磁条卡集合

    private String beginNumber;//批量起始卡号

    private String endNumber; //批量结束卡号

    private String shippingStatus;//物流状态

    private String cardIdentifier;//批次

    //================================getter and setter===============================


    public String getCardIdentifier() {
        return cardIdentifier;
    }

    public void setCardIdentifier(String cardIdentifier) {
        this.cardIdentifier = cardIdentifier;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getBeginNumber() {
        return beginNumber;
    }

    public void setBeginNumber(String beginNumber) {
        this.beginNumber = beginNumber;
    }

    public String getEndNumber() {
        return endNumber;
    }

    public void setEndNumber(String endNumber) {
        this.endNumber = endNumber;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getDiskCard() {
        return diskCard;
    }

    public void setDiskCard(String diskCard) {
        this.diskCard = diskCard;
    }

    public String getChipCard() {
        return chipCard;
    }

    public void setChipCard(String chipCard) {
        this.chipCard = chipCard;
    }

    public List<ExpresspayCardPojo> getDiskCardPojoList() {
        return diskCardPojoList;
    }

    public void setDiskCardPojoList(List<ExpresspayCardPojo> diskCardPojoList) {
        this.diskCardPojoList = diskCardPojoList;
    }

    public List<ExpresspayCardPojo> getChipCardPojoList() {
        return chipCardPojoList;
    }

    public void setChipCardPojoList(List<ExpresspayCardPojo> chipCardPojoList) {
        this.chipCardPojoList = chipCardPojoList;
    }
}