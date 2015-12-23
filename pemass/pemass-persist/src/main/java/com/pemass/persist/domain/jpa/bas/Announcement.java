package com.pemass.persist.domain.jpa.bas;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.AnnouncementTypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Description: Announcement
 * Author: estn.zuo
 * CreateTime: 2014-10-10 17:21
 */
@Entity
@Table(name = "bas_announcement")
public class Announcement extends BaseDomain {

    @Column(name = "title", length = 50, nullable = false)
    private String title;  //标题

    @Column(name = "content", length = 4000, nullable = false)
    private String content;  //内容

    @Column(name = "announcement_type",length = 20,nullable = false)
    @Enumerated(EnumType.STRING)
    private AnnouncementTypeEnum announcementType;  //公告类型

    @Column(name = "issue_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueTime;  //发布时间

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public AnnouncementTypeEnum getAnnouncementType() {
        return announcementType;
    }

    public void setAnnouncementType(AnnouncementTypeEnum announcementType) {
        this.announcementType = announcementType;
    }
}
