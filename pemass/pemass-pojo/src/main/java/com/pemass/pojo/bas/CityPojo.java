/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.bas;

import java.util.List;

/**
 * @Description: DevicePojo
 * @Author: estn.zuo
 * @CreateTime: 2015-04-14 16:03
 */
public class CityPojo {

    private Long id;

    private String cityName;

    private List<DistrictPojo> districtPojoList;


    //========================== getter and setter ==========================


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<DistrictPojo> getDistrictPojoList() {
        return districtPojoList;
    }

    public void setDistrictPojoList(List<DistrictPojo> districtPojoList) {
        this.districtPojoList = districtPojoList;
    }
}
