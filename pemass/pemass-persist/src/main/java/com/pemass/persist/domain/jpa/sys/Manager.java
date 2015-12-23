/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.sys;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.AccountStatusEnum;

import javax.persistence.*;

/**
 * @Description: Manager
 * @Author: estn.zuo
 * @CreateTime: 2014-10-15 16:22
 */
@Entity
@Table(name = "sys_manager")
public class Manager extends BaseDomain {

    @Column(name = "company_id",nullable = false)
    private Long companyId;  //机构

    @Column(name = "managername",length = 50,nullable = false)
    private String managername; //登录名(管理员名称)

    @Column(name = "password",length = 32,nullable = false)
    private String password;    //密码

    @Column(name = "salt",length = 8,nullable = false)
    private String salt;    //盐值

    @Column(name = "name",length = 50,nullable = false)
    private String name;    //名称

    @Column(name = "telephone",length = 20,nullable = false)
    private String telephone;   //联系电话

    @Column(name = "email",length = 50)
    private String email;   //邮箱

    @Column(name = "province_id", nullable = false)
    private Long provinceId; //省

    @Column(name = "city_id", nullable = false)
    private Long cityId;    //市

    @Column(name = "district_id")
    private Long districtId;    //区

    @Column(name = "location",length = 50,nullable = false)
    private String location;    //详细地址

    @Column(name = "account_status",nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AccountStatusEnum accountStatus;    //账号状态

    @Column(name = "authority", length = 100, nullable = false)
    private String authority;   //权限

    //=============getter and setter ===================


    public String getManagername() {
        return managername;
    }

    public void setManagername(String managername) {
        this.managername = managername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public AccountStatusEnum getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatusEnum accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }
}
