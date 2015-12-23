/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.bas.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.bas.Announcement;
import com.pemass.persist.enumeration.AnnouncementTypeEnum;
import com.pemass.service.pemass.bas.AnnouncementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: AnnouncementServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2014-10-15 17:18
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Resource
    private BaseDao jpaBaseDao;

    @Override
    public DomainPage selectPageByType(AnnouncementTypeEnum announcementType, long pageIndex, long pageSize) {
        List<Expression> expressions = new ArrayList<Expression>();
        expressions.add(new Expression("announcementType", Operation.Equal, announcementType));
        expressions.add(new Expression("issueTime", Operation.LessThanEqual, new Date()));
        return jpaBaseDao.getEntitiesPagesByExpressionList(Announcement.class, expressions, pageIndex, pageSize);
    }

    @Override
    public DomainPage selectPage(long pageIndex, long pageSize) {
        return jpaBaseDao.getAllEntitiesByPage(Announcement.class, pageIndex, pageSize);
    }

    @Override
    public Announcement insert(Announcement announcement) {
        jpaBaseDao.persist(announcement);
        return announcement;
    }

    @Override
    public Announcement update(Announcement announcement) {
        Announcement storedAnnouncement = jpaBaseDao.getEntityById(Announcement.class, announcement.getId());
        /**合并实体*/
        MergeUtil.merge(announcement, storedAnnouncement);

        jpaBaseDao.merge(storedAnnouncement);
        return storedAnnouncement;
    }

    @Override
    public Announcement selectById(Long id) {
        return jpaBaseDao.getEntityById(Announcement.class, id);
    }
}