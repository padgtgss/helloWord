/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.biz.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.dao.poi.PresentPackDao;
import com.pemass.persist.domain.jpa.biz.Notification;
import com.pemass.persist.domain.jpa.biz.NotificationOrganization;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.service.pemass.biz.NotificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: NotificationServiceImpl
 * @Author: he jun cheng
 * @CreateTime: 2015-01-05 11:04
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    @Resource
    private PresentPackDao presentPackDao;

    @Resource
    private BaseDao jpaBaseDao;
    @Override
    public void saveNotification(Notification notification, List<Organization> organizations) {
        //STEP ONE:SAVE NOTIFICATION
        presentPackDao.persist(notification);

        //STEP TWO:SAVE NOTIFICATION-ORGANIZATION
        if (organizations != null) {
            for (Organization organization : organizations) {
                NotificationOrganization no = new NotificationOrganization();
                no.setOrganizationId(organization.getId());
                no.setIsRead(false);
                no.setNotificationId(notification.getId());

                presentPackDao.persist(no);
            }
        }
    }

    @Override
    public Notification getNotificationById(Long notificationId) {
        return presentPackDao.getEntityById(Notification.class, notificationId);
    }

    @Override
    public DomainPage<Notification> getNotificationByConditions(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage) {
        return presentPackDao.getEntitiesPagesByFieldList(Notification.class, conditions, fuzzyConditions, domainPage.getPageIndex(), domainPage.getPageSize());
    }

    @Override
    public List<Object[]> getNotificationOrganizationList(Long notificationId) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("notificationId",notificationId);
        List<NotificationOrganization> list=jpaBaseDao.getEntitiesByFieldList(NotificationOrganization.class,map);
        List<Object[]> returnList=new ArrayList<Object[]>();
        if(list!=null){
            for (int i=0;i<list.size();i++){
                Object[] objects = new Object[2];
                NotificationOrganization not=list.get(i);
                objects[0]=not;
                Organization organization=jpaBaseDao.getEntityById(Organization.class,not.getOrganizationId());
                objects[1]=organization;
                returnList.add(objects);
            }
        }
        return returnList;
    }
}