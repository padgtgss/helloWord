/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.sys;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.CompanyTypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * @Description: Organization
 * @Author: estn.zuo
 * @CreateTime: 2014-10-15 16:15
 */
@Entity
@Table(name = "sys_company")
public class Company extends BaseDomain {

    @Column(name = "company_name", length = 50)
    private String companyName;  //公司名称

    @Column(name="company_type",length = 20)
    @Enumerated(EnumType.STRING)
    private CompanyTypeEnum companyType; //公司类型

    @Column(name = "link_man", length = 50)
    private String linkMan;    //联系人

    @Column(name = "link_phone", length = 20)
    private String linkPhone;  //联系电话

    @Column(name = "email", length = 50)
    private String email;   //邮箱

    @Column(name = "location", length = 50)
    private String location; //地址

    @Column(name = "area", length = 255)
    private String area;    //管辖区域(多个省份时，使用","分隔)

    //================getter and setter==================

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public CompanyTypeEnum getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyTypeEnum companyType) {
        this.companyType = companyType;
    }
}
