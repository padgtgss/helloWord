package com.pemass.service.pemass.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.biz.News;

import java.util.Map;


/**
 * 新闻管理
 * Created by hejch on 2014/12/10.
 */
public interface NewsService {

    DomainPage getNewsList(Map<String, Object> conditions, Long pageIndex, Long pageSize);

    News getNewsInfo(Long newsId);

    /**
     * 获取满足条件的新闻纪录
     *
     * @param conditions
     * @param fuzzyConditions
     * @param domainPage
     * @return
     */
    DomainPage getNewsRecord(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage);

    /**
     * 更新新闻纪录
     *
     * @param news
     * @return
     */
    News updateNews(News news);

    /**
     * 根据ID获取新闻信息
     *
     * @param id
     * @return
     */
    News getNewsById(Long id);

    /**
     * 添加新闻
     *
     * @param news
     */
    void addNews(News news);
}
