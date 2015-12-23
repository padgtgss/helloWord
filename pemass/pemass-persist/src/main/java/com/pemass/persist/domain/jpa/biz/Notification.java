/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * @Description: 消息记录表（推送消息时先记录此表，再记录NotificationOrganization）
 * 与NotificationOrganization的关系是一对多
 * @Author: he jun cheng
 * @CreateTime: 2015-01-05 10:25
 */
@Entity
@Table(name = "biz_notification")
public class Notification extends BaseDomain {

    @Column(name = "issuer", length = 50)
    private String issuer;//发布人

    @Column(name = "title", length = 50)
    private String title;//标题

    @Column(name = "summary", length = 400)
    private String summary;//简介

    @Column(name = "content", length = 2000)
    private String content;//内容

    @Column(name = "issue_time")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date issueTime;//发布时间
//
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "notification")
//    private Set<NotificationOrganization> notificationOrganizations;    //该条消息发给谁（这里的发给谁在notificationOrganizations中进行了处理）

    //========================== getter and setter ========================\\


    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public Set<NotificationOrganization> getNotificationOrganizations() {
//        return notificationOrganizations;
//    }
//
//    public void setNotificationOrganizations(Set<NotificationOrganization> notificationOrganizations) {
//        this.notificationOrganizations = notificationOrganizations;
//    }
}

