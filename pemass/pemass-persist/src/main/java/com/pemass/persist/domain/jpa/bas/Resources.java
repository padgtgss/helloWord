/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.bas;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.ResourceType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * @Description: Resource
 * @Author: estn.zuo
 * @CreateTime: 2014-10-14 11:16
 */
@Entity
@Table(name = "bas_resources")
public class Resources extends BaseDomain {

    @Column(name = "target_uuid", nullable = false, length = 32)
    private String targetUUID;  //所属目标ID

    @Column(name = "resource_type", nullable = false)
    @Enumerated
    private ResourceType resourceType;   //资源类型

    @Column(name = "title", length = 50)
    private String title;   //资源标题

    @Column(name = "summary", length = 400)
    private String summary; //资源简介

    @Column(name = "url", length = 200)
    private String url; //资源url地址

    @Column(name = "sequence")
    private Double sequence; //序号

    public String getTargetUUID() {
        return targetUUID;
    }

    public void setTargetUUID(String targetUUID) {
        this.targetUUID = targetUUID;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getSequence() {
        return sequence;
    }

    public void setSequence(Double sequence) {
        this.sequence = sequence;
    }
}
