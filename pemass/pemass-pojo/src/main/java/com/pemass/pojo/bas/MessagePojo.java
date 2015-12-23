/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.bas;

import com.pemass.pojo.sys.BodyPojo;

import java.util.Date;
import java.util.List;

/**
 * @Description: MessagePojo
 * @Author: estn.zuo
 * @CreateTime: 2014-12-23 10:11
 */
public class MessagePojo {

    //消息ID
    private String id;

    //消息标题
    private String title;

    //消息简介
    private String summary;

    //详情
    private List<BodyPojo> detail;

    //发布时间
    private Date issueTime;

    //是否查看
    private Boolean hasRead;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public List<BodyPojo> getDetail() {
        return detail;
    }

    public void setDetail(List<BodyPojo> detail) {
        this.detail = detail;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public Boolean getHasRead() {
        return hasRead;
    }

    public void setHasRead(Boolean hasRead) {
        this.hasRead = hasRead;
    }
}
