package com.pemass.persist.domain.jpa.bas;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Description: District
 * Author: estn.zuo
 * CreateTime: 2014-10-10 17:40
 */

@Entity
@Table(name = "bas_district")
public class District extends BaseDomain {

    @JoinColumn(name = "city_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private City city;

    @Column(name = "district_name", length = 50, nullable = false)
    protected String districtName;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}
