/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.core.pojo.Body;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.NewsTypeEnum;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * @Description: 新闻内容表
 * @Author: huang zhuo
 * @CreateTime: 2014-11-14 14:59
 */
@Entity
@Table(name = "biz_news")
public class News extends BaseDomain {

    @Column(name = "title", length = 50)
    private String title;//标题

    @Column(name = "summary", length = 200)
    private String summary;//简介

    @Column(name = "preview_image", length = 200)
    private String previewImage;//缩略图

    @Type(type = "com.pemass.persist.type.BodyUserType")
    @Column(name = "content", length = 4000)
    private List<Body> content;//内容

    @Column(name = "news_type", length = 32)
    @Enumerated(EnumType.STRING)
    private NewsTypeEnum newsType;//新闻类型

    @Column(name = "issue_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueTime;   //发布时间

    @Column(name = "sequence", nullable = false)
    private Double sequence;   //序列号

    //=============getter and setter ===================

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

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }

    public List<Body> getContent() {
        return content;
    }

    public void setContent(List<Body> content) {
        this.content = content;
    }

    public NewsTypeEnum getNewsType() {
        return newsType;
    }

    public void setNewsType(NewsTypeEnum newsType) {
        this.newsType = newsType;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public Double getSequence() {
        return sequence;
    }

    public void setSequence(Double sequence) {
        this.sequence = sequence;
    }
}

