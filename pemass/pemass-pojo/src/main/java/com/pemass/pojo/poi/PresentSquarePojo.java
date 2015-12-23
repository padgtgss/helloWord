/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;

import com.pemass.pojo.serializer.ResourceUrlSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * @Description: PresentSquarePojo
 * @Author: zhou hang
 * @CreateTime: 2014-12-10 17:06
 */
public class PresentSquarePojo {

    private Date expiryTime;    //有效期

    private String sourceName;//来源名称(如果是商户包就是商户的名称，如果是用户就是用户的名称)

    private String strategyName;    //策略名

    private Integer total;//红包个数

    private Long organizationId;//商家id

    private Long issueStrategyId;//策略批次id

    private boolean status ;//状态(true-已领取、)

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String logo;  //商家logo地址

    //====================getter setter ===================


    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getIssueStrategyId() {
        return issueStrategyId;
    }

    public void setIssueStrategyId(Long issueStrategyId) {
        this.issueStrategyId = issueStrategyId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}