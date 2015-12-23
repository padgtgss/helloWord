/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.pojo.poi;

/**
 * @Description: CollectMoneyStrategySnapshotPojo
 * @Author: estn.zuo
 * @CreateTime: 2015-08-18 15:48
 */
public class CollectMoneyStrategySnapshotPojo {

    private String strategyName;    //策略名

    private Integer isDiscount; //是否进行收款折扣【0-不折扣、1-折扣】

    private Double discount; //收款折扣

    private String givingPonitPDesc; //赠送E积分描述信息

    private String givingPonitEDesc; //赠送E通币描述信息

    private String organizationName;    //商户名称

    public Integer getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(Integer isDiscount) {
        this.isDiscount = isDiscount;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getGivingPonitPDesc() {
        return givingPonitPDesc;
    }

    public void setGivingPonitPDesc(String givingPonitPDesc) {
        this.givingPonitPDesc = givingPonitPDesc;
    }

    public String getGivingPonitEDesc() {
        return givingPonitEDesc;
    }

    public void setGivingPonitEDesc(String givingPonitEDesc) {
        this.givingPonitEDesc = givingPonitEDesc;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
