
package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.domain.jpa.sys.Organization;

import javax.persistence.*;

/**
 * Description: 通知信息表（站内信中间表）
 * Author: he jun cheng
 * CreateTime: 2014-01-15 14:15
 */
@Entity
@Table(name = "biz_notification_organization")
public class NotificationOrganization extends BaseDomain {

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;//收件人

    @Column(name = "notification_id", nullable = false)
    private Long notificationId; //对应消息记录表

    @Column(name = "is_read")
    private Boolean isRead;//阅读状态【true-已读、false-未读】

    //================================= getter and setter ==============================\\


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

}