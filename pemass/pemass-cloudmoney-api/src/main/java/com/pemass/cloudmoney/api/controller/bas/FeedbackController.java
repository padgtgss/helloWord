/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.bas;

import com.google.common.collect.ImmutableMap;
import com.pemass.persist.domain.jpa.sys.Feedback;
import com.pemass.service.pemass.bas.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description: FeedbackController
 * @Author: zhou hang
 * @CreateTime: 2014-11-17 11:30
 */
@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Resource
    private FeedbackService feedbackService;

    /**
     * 添加意见反馈信息
     *
     * @param feedback 意见反馈对象
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object insert(Feedback feedback) {
        feedback = feedbackService.insert(feedback);
        return ImmutableMap.of("result", feedback.getId() != null);
    }

}