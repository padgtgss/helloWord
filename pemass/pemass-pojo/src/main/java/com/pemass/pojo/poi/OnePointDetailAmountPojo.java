/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;

import java.util.List;

/**
 * @Description: OnePointDetailCountPojo
 * @Author: zhou hang
 * @CreateTime: 2015-06-12 14:02
 */
public class OnePointDetailAmountPojo {

    private Integer dueAmount; //应还总额

    private Integer residualAmount; //我可利用数目

    private Integer giveAmount;//朋友未用数目

    private Integer repaymentAmount;//可还款总额

    private List<OnePointDetailPojo> detailPojoList;

    //====================getter and setter=================


    public List<OnePointDetailPojo> getDetailPojoList() {

        return detailPojoList;
    }

    public void setDetailPojoList(List<OnePointDetailPojo> detailPojoList) {
        this.detailPojoList = detailPojoList;
    }

    public Integer getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Integer dueAmount) {
        this.dueAmount = dueAmount;
    }

    public Integer getResidualAmount() {
        return residualAmount;
    }

    public void setResidualAmount(Integer residualAmount) {
        this.residualAmount = residualAmount;
    }

    public Integer getGiveAmount() {
        return giveAmount;
    }

    public void setGiveAmount(Integer giveAmount) {
        this.giveAmount = giveAmount;
    }

    public Integer getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Integer repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }
}