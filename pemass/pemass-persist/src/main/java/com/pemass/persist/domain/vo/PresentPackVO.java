/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.vo;

import com.google.common.collect.Lists;
import com.pemass.persist.enumeration.PresentPackTypeEnum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 包红包的时候使用的传值对象
 * @Author: oliver.he
 * @CreateTime: 2015-08-05 15:42
 */
public class PresentPackVO implements Serializable, Cloneable {

    private static final long serialVersionUID = 6640928001312031854L;

    // 商户ID
    private Long organizationId;

    // 商户名字
    private String organizationName;

    // 红包名称
    private String presentPackName;

    // 红包个数
    private Integer packTotalAmount;

    // 红包使用说明
    private String instruction;

    // 红包类型
    private PresentPackTypeEnum presentPackType;

    // 红包的区域
    private String area;

    //发行时间
    private Date issueTime;

    // 红包的过期时间
    private Date expiryTime;

    // 包红包所选用的积分
    private List<PresentPointVO> presentPoints;

    @Override
    public PresentPackVO clone() {
        PresentPackVO packVo = null;
        try {
            packVo = (PresentPackVO) super.clone();

            List<PresentPointVO> pointVos = Lists.newArrayList();
            for (PresentPointVO pointVo : presentPoints) {
                pointVos.add(pointVo.clone());
            }

            packVo.presentPoints = pointVos;
        } catch (CloneNotSupportedException e) {
            System.err.println("clone happened exception." + this);
        }
        return packVo;
    }

    // ============================ \\


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getPresentPackName() {
        return presentPackName;
    }

    public void setPresentPackName(String presentPackName) {
        this.presentPackName = presentPackName;
    }

    public Integer getPackTotalAmount() {
        return packTotalAmount;
    }

    public void setPackTotalAmount(Integer packTotalAmount) {
        this.packTotalAmount = packTotalAmount;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public PresentPackTypeEnum getPresentPackType() {
        return presentPackType;
    }

    public void setPresentPackType(PresentPackTypeEnum presentPackType) {
        this.presentPackType = presentPackType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public List<PresentPointVO> getPresentPoints() {
        return presentPoints;
    }

    public void setPresentPoints(List<PresentPointVO> presentPoints) {
        this.presentPoints = presentPoints;
    }
}