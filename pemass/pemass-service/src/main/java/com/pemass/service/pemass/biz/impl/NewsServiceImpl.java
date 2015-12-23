/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.biz.impl;


import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.biz.NewsDao;
import com.pemass.persist.dao.poi.PresentPackDao;
import com.pemass.persist.domain.jpa.biz.News;
import com.pemass.service.pemass.biz.NewsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: NewsServiceImpl
 * @Author: pokl.huang
 * @CreateTime: 2014-12-09 09:55
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Resource
    private NewsDao newsDao;

    @Resource
    private PresentPackDao presentPackDao;

    @Override
    public DomainPage getNewsList(Map<String, Object> conditions, Long pageIndex, Long pageSize) {

        return newsDao.getEntitiesPagesByFieldList(News.class, conditions, "sequence", BaseDao.OrderBy.DESC, pageIndex, pageSize);
    }

    @Override
    public News getNewsInfo(Long newsId) {
        return newsDao.getEntityByField(News.class, "id", newsId);
    }


    @Override
    public DomainPage getNewsRecord(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage) {
        Map<String, BaseDao.OrderBy> orderByConditions = new HashMap<String, BaseDao.OrderBy>();
        orderByConditions.put("sequence", BaseDao.OrderBy.DESC);
        orderByConditions.put("issueTime", BaseDao.OrderBy.DESC);
        return presentPackDao.getEntitiesPagesByFieldList(News.class, conditions, fuzzyConditions, null, null, orderByConditions, domainPage.getPageIndex(), domainPage.getPageSize());
    }

    @Override
    public News updateNews(News news) {
        News targetNews = presentPackDao.getEntityById(News.class, news.getId());
        targetNews = (News) MergeUtil.merge(news, targetNews);
        return presentPackDao.merge(targetNews);
    }

    @Override
    public News getNewsById(Long id) {
        return presentPackDao.getEntityById(News.class, id);
    }

    @Override
    public void addNews(News news) {
        presentPackDao.persist(news);
    }
}