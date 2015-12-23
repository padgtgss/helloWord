/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;

import com.pemass.persist.enumeration.PresentSourceTypeEnum;
import com.pemass.persist.enumeration.PresentStatusEnum;

import java.util.Date;
import java.util.List;

/**
 * @Description: 红包
 * @Author: zhou hang
 * @CreateTime: 2014-11-26 19:50
 */
public class PresentPojo {

    private Long id;//ID

    private PresentStatusEnum presentStatus; // 红包状态

    private String presentName;    // 红包名称

    private Long userId;  // 用户 (红包所有者，发给用户的时候)

    private PresentSourceTypeEnum presentSourceType;  //红包来源类型

    private String sourceName; // 来源名称(如果是商户包就是商户的名称，如果是用户就是用户的名称)

    private String instruction; // 使用说明 冗余字段，继承自PresentPack

    private Date expiryTime;    // 过期时间 冗余字段，继承自PresentPack

    private Date issueTime;  //发行时间

    private List<PresentItemPojo> presentItemPojoList;//红包项列表

    //====================================getter and setter=============================================


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPresentName() {
        return presentName;
    }

    public void setPresentName(String presentName) {
        this.presentName = presentName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public List<PresentItemPojo> getPresentItemPojoList() {
        return presentItemPojoList;
    }

    public void setPresentItemPojoList(List<PresentItemPojo> presentItemPojoList) {
        this.presentItemPojoList = presentItemPojoList;
    }

    public PresentSourceTypeEnum getPresentSourceType() {
        return presentSourceType;
    }

    public void setPresentSourceType(PresentSourceTypeEnum presentSourceType) {
        this.presentSourceType = presentSourceType;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public PresentStatusEnum getPresentStatus() {
        return presentStatus;
    }

    public void setPresentStatus(PresentStatusEnum presentStatus) {
        this.presentStatus = presentStatus;
    }
}