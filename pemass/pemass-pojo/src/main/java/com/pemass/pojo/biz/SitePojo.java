package com.pemass.pojo.biz;

import com.pemass.pojo.poi.CollectMoneyStategyPojo;
import com.pemass.pojo.serializer.ResourceUrlSerializer;
import com.pemass.pojo.sys.BodyPojo;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

/**
 * @Description: ProductController
 * @Author: huang zhuo
 * @CreateTime: 2014-11-10 11:40
 */

public class SitePojo {

    private Long id;

    private String siteName;    //营业点名称

    private Integer isOneMerchant; //是否受理一元购积分商户【加速商户】（0、不是 1、是）

    private Integer isSpecial; //是否是特约商户（0、不是 1、是）

    private List<BodyPojo> summary; //简介

    private String content; //描述

    private String serviceTime; //营业时间段 如：9:30--17:00

    private String sitePhone;   //场所电话

    private String longitude;   //经度

    private String latitude;    //纬度

    private String location;    //详细地址

    private Double distance;//与用户当前坐标的距离

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String previewImage;//营业点预览图

    private List<SiteImagePojo> siteImagePojoList;//图集

    private CollectMoneyStategyPojo collectMoneyStategyPojo;//当前收款策略
    //======================== getter and setter ====================================


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public List<BodyPojo> getSummary() {
        return summary;
    }

    public void setSummary(List<BodyPojo> summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
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

    public List<SiteImagePojo> getSiteImagePojoList() {
        return siteImagePojoList;
    }

    public void setSiteImagePojoList(List<SiteImagePojo> siteImagePojoList) {
        this.siteImagePojoList = siteImagePojoList;
    }

    public Integer getIsOneMerchant() {
        return isOneMerchant;
    }

    public void setIsOneMerchant(Integer isOneMerchant) {
        this.isOneMerchant = isOneMerchant;
    }

    public Integer getIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(Integer isSpecial) {
        this.isSpecial = isSpecial;
    }

    public CollectMoneyStategyPojo getCollectMoneyStategyPojo() {
        return collectMoneyStategyPojo;
    }

    public void setCollectMoneyStategyPojo(CollectMoneyStategyPojo collectMoneyStategyPojo) {
        this.collectMoneyStategyPojo = collectMoneyStategyPojo;
    }
}
