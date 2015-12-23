/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.biz.Notification;
import com.pemass.persist.domain.jpa.sys.Organization;

import java.util.List;
import java.util.Map;

/**
 * @Description: 站内信
 * @Author: he jun cheng
 * @CreateTime: 2015-01-05 10:55
 */
public interface NotificationService {
    /**
     * 新增站内信（并将此站内信发送给对应商户）
     *
     * @param notification
     * @param organizations
     */
    void saveNotification(Notification notification, List<Organization> organizations);


    /**
     * 根据ID获取某一条站内信的详情
     *
     * @param notificationId
     * @return
     */
    Notification getNotificationById(Long notificationId);

    /**
     * 获取满足条件的站内信记录
     *
     * @param conditions
     * @param fuzzyConditions
     * @param domainPage
     * @return
     */
    DomainPage<Notification> getNotificationByConditions(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage);

    /**
     * 根据notificationId查询发送给相关的Organization信息
     * @param notificationId
     * @return
     */
    List<Object[]> getNotificationOrganizationList(Long notificationId);
}