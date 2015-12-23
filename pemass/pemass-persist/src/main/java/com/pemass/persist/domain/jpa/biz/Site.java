package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.core.pojo.Body;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.common.server.enumeration.GenderEnum;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.List;

/**
 * 营业点
 *
 * @author 向阳
 */
@Entity
@Table(name = "biz_site")
public class Site extends BaseDomain {

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;//所属账户

    @Column(name = "site_name", length = 50, nullable = false)
    private String siteName;    //营业点名称

    @Type(type = "com.pemass.persist.type.BodyUserType")
    @Column(name = "summary", length = 4000)
    private List<Body> summary; //简介

    @Column(name = "content", length = 4000)
    private String content; //描述

    @Column(name = "service_time", length = 20)
    private String serviceTime; //营业时间段 如：9:30--17:00

    @Column(name = "site_phone", length = 20, nullable = false)
    private String sitePhone;   //场所电话

    @Column(name = "link_man", length = 50, nullable = false)
    private String linkMan; //联系人姓名

    @Column(name = "gender", length = 6, nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum gender; //联系人性别

    @Column(name = "email", length = 50)
    private String email;   //联系人邮箱

    @Column(name = "link_phone", length = 50, nullable = false)
    private String linkPhone;   //联系人电话

    @Column(name = "longitude", length = 15)
    private String longitude;   //经度

    @Column(name = "latitude", length = 15)
    private String latitude;    //纬度

    @Column(name = "location", length = 50, nullable = false)
    private String location;    //详细地址

    @Column(name = "province_id", nullable = false)
    private Long provinceId; //省

    @Column(name = "city_id", nullable = false)
    private Long cityId;    //市

    @Column(name = "district_id", nullable = false)
    private Long districtId;    //区


    //=================== getter and setter =========


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Body> getSummary() {
        return summary;
    }

    public void setSummary(List<Body> summary) {
        this.summary = summary;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getSitePhone() {
        return sitePhone;
    }

    public void setSitePhone(String sitePhone) {
        this.sitePhone = sitePhone;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
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
