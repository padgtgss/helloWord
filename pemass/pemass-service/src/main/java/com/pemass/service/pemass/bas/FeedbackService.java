/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.bas;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.sys.Feedback;

import java.util.Map;

/**
 * @Description: 意见反馈
 * @Author: zhou hang
 * @CreateTime: 2014-11-17 11:32
 */
public interface FeedbackService {
    /**
     * 添加意见反馈信息
     *
     * @param feedback 反馈对象
     * @return
     */
    Feedback insert(Feedback feedback);

    /**
     * 获取满足条件的意见反馈
     *
     * @param conditions
     * @param fuzzyConditions
     * @param domainPage
     * @return
     */
    DomainPage getFeedbackRecord(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage);

    /**
     * 更新意见反馈
     *
     * @param feedback
     * @return
     */
    Feedback updateFeedback(Feedback feedback);

    /**
     * 根据ID获取意见反馈信息
     *
     * @param id
     * @return
     */
    Feedback getFeedbackById(Long id);
}
