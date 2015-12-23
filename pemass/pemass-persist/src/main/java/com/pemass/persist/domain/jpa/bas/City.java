package com.pemass.persist.domain.jpa.bas;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Description: City
 * Author: estn.zuo
 * CreateTime: 2014-10-10 17:31
 */
@Entity
@Table(name = "bas_city")
public class City extends BaseDomain {

    @JoinColumn(name = "province_id",referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Province province;

    @Column(name = "city_name", length = 50, nullable = false)
    private String cityName;

    @Column(name = "zip_code", length = 10, nullable = false)
    private String zipCode;

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
