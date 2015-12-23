/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.biz;



import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.annotation.Auth;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.biz.UserExpresspayCard;
import com.pemass.persist.domain.jpa.biz.UserExpresspayCardDetail;
import com.pemass.pojo.biz.UserExpresspayCardPojo;
import com.pemass.service.exception.BizError;
import com.pemass.service.pemass.biz.UserExpresspayCardService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * @Description: UserExpresspayCardController
 * @Author: pokl.huang
 * @CreateTime: 2014-11-18 09:40
 */
@Controller
@RequestMapping("/userExpresspayCard")
public class UserExpresspayCardController {

    private Log logger = LogFactory.getLog(UserExpresspayCardController.class);

    @Resource
    private UserExpresspayCardService userExpresspayCardService;

    /**
     * 获取用户银通卡
     *
     * @param uid
     * @return
     */
    @Auth(entity = UserExpresspayCard.class,parameter = "uid")
    @RequestMapping(value = "/user/{uid}",method = RequestMethod.GET)
    @ResponseBody
    public Object getuserExpresspayCard(@PathVariable("uid") Long uid) {
        UserExpresspayCard userExpresspayCard = userExpresspayCardService.getByUserId(uid);
        if(userExpresspayCard != null)
            return   MergeUtil.merge(userExpresspayCard, new UserExpresspayCardPojo());
        else{
           return new UserExpresspayCardPojo();
        }
    }

    /**
     * 绑定银通卡
     *
     * @param uid
     * @param cardNumber
     * @return
     */
    @Auth(entity = UserExpresspayCard.class,parameter = "uid")
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    @ResponseBody
    public Object binduserExpresspayCard(Long uid,String cardNumber) {
        return MergeUtil.merge(userExpresspayCardService.bindExpresspayCard(uid, cardNumber), new UserExpresspayCardPojo());
    }


    /**
     * 解绑银通卡
     *
     * @param uid
     * @param cardNumber
     * @return
     */
    @Auth(entity = UserExpresspayCard.class,parameter = "uid")
    @RequestMapping(value = "/unbind", method = RequestMethod.POST)
    @ResponseBody
    public Object unbinduserExpresspayCard(Long uid, String cardNumber) {

        return  ImmutableMap.of("result", userExpresspayCardService.unBindExpresspayCard(uid, cardNumber));
    }



}

