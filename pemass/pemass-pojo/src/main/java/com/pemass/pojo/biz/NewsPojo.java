package com.pemass.pojo.biz;

import com.pemass.persist.enumeration.NewsTypeEnum;
import com.pemass.pojo.serializer.ResourceUrlSerializer;
import com.pemass.pojo.sys.BodyPojo;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * @Description: ProductController
 * @Author: huang zhuo
 * @CreateTime: 2014-11-10 11:40
 */

public class NewsPojo {

    private Long id;

    private String title;//标题

    private String summary;//简介

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String previewImage;//缩略图

    private List<BodyPojo> content;//内容

    private NewsTypeEnum newsType;//新闻类型

    private Date issueTime;   //发布时间

    private Float sequence;   //序列号

   //======================== getter and setter ====================================


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }

    public List<BodyPojo> getContent() {
        return content;
    }

    public void setContent(List<BodyPojo> content) {
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

    public Float getSequence() {
        return sequence;
    }

    public void setSequence(Float sequence) {
        this.sequence = sequence;
    }
}
