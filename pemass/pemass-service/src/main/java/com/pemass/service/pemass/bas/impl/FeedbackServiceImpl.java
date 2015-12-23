/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.bas.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.poi.PresentPackDao;
import com.pemass.persist.domain.jpa.sys.Feedback;
import com.pemass.service.pemass.bas.FeedbackService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description: FeedbackServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2014-11-17 11:39
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Resource
    private PresentPackDao presentPackDao;

    /**
     * 添加意见反馈信息
     *
     * @param feedback 反馈对象
     * @return
     */
    @Override
    public Feedback insert(Feedback feedback) {
        presentPackDao.persist(feedback);
        return feedback;
    }

    @Override
    public DomainPage getFeedbackRecord(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage) {
        return presentPackDao.getEntitiesPagesByFieldList(Feedback.class, conditions, fuzzyConditions, domainPage.getPageIndex(), domainPage.getPageSize());
    }

    @Override
    public Feedback updateFeedback(Feedback feedback) {
        Feedback targetFeedback = presentPackDao.getEntityById(Feedback.class, feedback.getId());
        targetFeedback = (Feedback) MergeUtil.merge(feedback, targetFeedback);
        return presentPackDao.merge(targetFeedback);
    }

    @Override
    public Feedback getFeedbackById(Long id) {
        return presentPackDao.getEntityById(Feedback.class, id);
    }

}