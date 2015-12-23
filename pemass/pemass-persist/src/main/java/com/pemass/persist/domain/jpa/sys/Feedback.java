/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.sys;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.FeedbackTypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * @Description: 意见反馈
 * @Author: huang zhuo
 * @CreateTime: 2014-11-14 14:53
 */
@Entity
@Table(name = "sys_feedback")
public class Feedback extends BaseDomain {

    @Column(name = "target_id")
    private Long targetId;   //目标ID;

    @Column(name = "feedback_type")
    @Enumerated(EnumType.ORDINAL)
    private FeedbackTypeEnum feedbackType;   //反馈类型

    @Column(name = "title",length = 50)
    private String title;   //标题;

    @Column(name = "content",length = 2000)
    private String content;   //内容;

    //=============getter and setter ===================

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public FeedbackTypeEnum getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(FeedbackTypeEnum feedbackType) {
        this.feedbackType = feedbackType;
    }

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
}

