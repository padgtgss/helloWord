/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.jpa.sys;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Description: 组织机构和机具关系表
 * @Author: estn.zuo
 * @CreateTime: 2015-07-13 14:24
 */
@Entity
@Table(name = "sys_organization_pos")
public class OrganizationPos extends BaseDomain {

    @Column(name = "organization_id",nullable = false)
    private Long organizationId;    //组织机构ID

    @Column(name = "pos_serial", nullable = false,length = 50)
    private String posSerial;   //机具编号

    @Column(name = "pos_model")
    private String posModel;    //机具型号

    @Column(name = "bind_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bindTime;  //绑定时间

    //=================== getter and setter =========


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getPosSerial() {
        return posSerial;
    }

    public void setPosSerial(String posSerial) {
        this.posSerial = posSerial;
    }

    public String getPosModel() {
        return posModel;
    }

    public void setPosModel(String posModel) {
        this.posModel = posModel;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }
}
