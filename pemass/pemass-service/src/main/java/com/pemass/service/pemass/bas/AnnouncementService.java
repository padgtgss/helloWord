package com.pemass.service.pemass.bas;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.bas.Announcement;
import com.pemass.persist.enumeration.AnnouncementTypeEnum;

/**
 * Created by zhou on 2014/10/15.
 */
public interface AnnouncementService {

    /**
     * 查询公告信息
     *
     * @param announcementType 公告分类
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectPageByType(AnnouncementTypeEnum announcementType, long pageIndex, long pageSize);


    /**
     * 查询公告信息
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectPage(long pageIndex, long pageSize);


    /**
     * 插入一条公告信息
     *
     * @param announcement
     * @return
     */
    Announcement insert(Announcement announcement);

    /**
     * 更新公告信息
     *
     * @param announcement
     * @return
     */
    Announcement update(Announcement announcement);

    /**
     * 根据公告ID来查询公告信息
     *
     * @param id
     * @return
     */
    Announcement selectById(Long id);

}
