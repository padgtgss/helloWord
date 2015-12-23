/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.sys;

/**
 * @Description: OrganizationPojo
 * @Author: zhou hang
 * @CreateTime: 2015-04-20 15:32
 */
public class OrganizationPojo {

    private Long id;

    private String organizationName;

    private String location;

    private String organizationPhone; //机构电话

    //=========================getter and stter==========================


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationPhone() {
        return organizationPhone;
    }

    public void setOrganizationPhone(String organizationPhone) {
        this.organizationPhone = organizationPhone;
    }
}