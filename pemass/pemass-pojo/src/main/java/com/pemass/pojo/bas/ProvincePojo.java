/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.bas;

import java.util.List;

/**
 * @Description: ProvincePojo
 * @Author: zhou hang
 * @CreateTime: 2015-05-29 11:25
 */
public class ProvincePojo {

    private Long id;

    private String provinceName;

    private List<CityPojo> cityPojoList;

    //=============================getter and setter===============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public List<CityPojo> getCityPojoList() {
        return cityPojoList;
    }

    public void setCityPojoList(List<CityPojo> cityPojoList) {
        this.cityPojoList = cityPojoList;
    }
}