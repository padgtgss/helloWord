/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;

import java.util.Date;

/**
 * @Description: 赠送红包记录
 * @Author: zhou hang
 * @CreateTime: 2015-01-07 15:05
 */
public class PresentRecordPojo {

    private Long presentId;//红包id

    private Date givingTime;   //赠送时间

    private String presentName;//红包名称

    private String toUsername;//赠送给谁

    //================getter setter=================

    public Long getPresentId() {
        return presentId;
    }

    public void setPresentId(Long presentId) {
        this.presentId = presentId;
    }

    public String getPresentName() {
        return presentName;
    }

    public void setPresentName(String presentName) {
        this.presentName = presentName;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public Date getGivingTime() {
        return givingTime;
    }

    public void setGivingTime(Date givingTime) {
        this.givingTime = givingTime;
    }
}