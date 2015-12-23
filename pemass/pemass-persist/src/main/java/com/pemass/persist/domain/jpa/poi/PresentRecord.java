/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Description: 赠送红包记录
 * @Author: zhou hang
 * @CreateTime: 2014-12-24 10:59
 */
@Entity
@Table(name = "poi_present_record")
public class PresentRecord extends BaseDomain {

    @Column(name = "present_id",nullable = false)
    private Long presentId;

    @Column(name = "from_user_id",nullable = false)
    private Long fromUserId; //

    @Column(name = "to_user_id")
    private Long toUserId; //

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "giving_time",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date givingTime;//赠送时间
  //==============================getter setter===============================


    public Long getPresentId() {
        return presentId;
    }

    public void setPresentId(Long presentId) {
        this.presentId = presentId;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Date getGivingTime() {
        return givingTime;
    }

    public void setGivingTime(Date givingTime) {
        this.givingTime = givingTime;
    }
}