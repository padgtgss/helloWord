/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.vo;

/**
 * @Description: 积分区域值对象
 * @Author: estn.zuo
 * @CreateTime: 2015-07-23 15:56
 */
public class PointAreaVO {

    //省份ID
    private Long provinceId;

    //城市ID
    private Long cityId;

    //地区ID
    private Long districtId;

    //营业点ID
    private Long siteId;

    //机构ID
    private Long organizationId;

    public PointAreaVO() {
    }

    public PointAreaVO(Long provinceId, Long cityId, Long districtId, Long siteId, Long organizationId) {
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.districtId = districtId;
        this.siteId = siteId;
        this.organizationId = organizationId;
    }

    public PointAreaVO(Long provinceId, Long cityId, Long districtId) {
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.districtId = districtId;
    }

    public PointAreaVO(Long organizationId) {
        this.organizationId = organizationId;
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

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
