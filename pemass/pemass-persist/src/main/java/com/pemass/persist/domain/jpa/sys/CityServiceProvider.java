package com.pemass.persist.domain.jpa.sys;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *  城市服务商
 */
@Entity
@Table(name = "sys_city_service_provider")
public class CityServiceProvider extends BaseDomain {

    @Column(name = "city_service_identifier", length = 10, nullable = false)
    private String cityServiceIdentifier;  //城市服务商编号

    @Column(name = "city_service_name", length = 50)
    private String cityServiceName;  //城市服务商名称

    @Column(name = "city_service_phone", length = 20)
    private String cityServicePhone; //联系电话

    @Column(name = "business", length = 2000)
    private String business;     //经营内容

    @Column(name = "location", length = 50)
    private String location; //地址

    @Column(name = "province_id")
    private Long provinceId; //省

    @Column(name = "city_id")
    private Long cityId;    //市

    @Column(name = "district_id")
    private Long districtId;    //区

    public String getCityServiceIdentifier() {
        return cityServiceIdentifier;
    }

    public void setCityServiceIdentifier(String cityServiceIdentifier) {
        this.cityServiceIdentifier = cityServiceIdentifier;
    }

    public String getCityServiceName() {
        return cityServiceName;
    }

    public void setCityServiceName(String cityServiceName) {
        this.cityServiceName = cityServiceName;
    }

    public String getCityServicePhone() {
        return cityServicePhone;
    }

    public void setCityServicePhone(String cityServicePhone) {
        this.cityServicePhone = cityServicePhone;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
