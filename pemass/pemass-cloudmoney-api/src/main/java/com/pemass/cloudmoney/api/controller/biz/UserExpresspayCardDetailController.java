/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.biz;


import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.annotation.Auth;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.biz.UserExpresspayCardDetail;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.enumeration.ConfigEnum;
import com.pemass.pojo.biz.UserExpresspayCardDetailPojo;
import com.pemass.service.exception.BizError;
import com.pemass.service.pemass.biz.UserExpresspayCardDetailService;
import com.pemass.service.pemass.sys.ConfigService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @Description: UserExpresspayCardController
 * @Author: pokl.huang
 * @CreateTime: 2014-11-18 09:40
 */
@Controller
@RequestMapping("/userExpresspayCardDetail")
public class UserExpresspayCardDetailController {

    private Log logger = LogFactory.getLog(UserExpresspayCardDetailController.class);

    @Resource
    private UserExpresspayCardDetailService userExpresspayCardDetailService;

    @Resource
    private ConfigService configService;

    /**
     * 获取用户银通卡圈存记录
     *
     * @param uid
     * @return
     */
    @Auth(entity = UserExpresspayCardDetail.class,parameter = "uid")
    @RequestMapping(value = "/search",method = RequestMethod.GET)
    @ResponseBody
    public Object getuserExpresspayCardDetail(Long uid,Long pageIndex,Long pageSize) {

        DomainPage domainPage = userExpresspayCardDetailService.selectDetailByUid(uid, pageIndex, pageSize);

        List<UserExpresspayCardDetail> userExpresspayCardDetailList = domainPage.getDomains();
        List<UserExpresspayCardDetailPojo> userExpresspayCardDetailPojoList = new ArrayList<UserExpresspayCardDetailPojo>();

        for (UserExpresspayCardDetail userExpresspayCardDetail : userExpresspayCardDetailList){
            UserExpresspayCardDetailPojo userExpresspayCardDetailPojo = new UserExpresspayCardDetailPojo();
            MergeUtil.merge(userExpresspayCardDetail,userExpresspayCardDetailPojo);
            userExpresspayCardDetailPojoList.add(userExpresspayCardDetailPojo);
        }

        domainPage.setDomains(userExpresspayCardDetailPojoList);
        return domainPage;
    }


    /**
     * 银通卡圈存
     *
     * @param uid
     * @return
     */
    @Auth(entity = UserExpresspayCardDetail.class,parameter = "uid")
    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    @ResponseBody
    public Object insertUserExpresspayCardDetail(Long uid, Integer pointEAmount,Float poundage,Float money) {
       /* if(pointEAmount<=0)
            throw new BaseBusinessException(BizError.POINT_E_AMOUNT_NOT_CORRECT);
        if(money <=0 )
            throw  new BaseBusinessException(BizError.MONEY_NOT_CORRECT);

        *//**计算手续费**//*
        Config config = configService.getConfigByKey(ConfigEnum.DEPOSIR_POUNDAGE);
        Float poundage_rate = Float.valueOf(config.getValue());
        Float result = pointEAmount *   poundage_rate;
        if (!poundage.equals(result) ){
            throw new BaseBusinessException(BizError.DEPOSIT_NOT_CORRECT);
        }

        if (!money.equals(pointEAmount - result)) {
            throw new BaseBusinessException(BizError.DEPOSIT_NOT_CORRECT);
        }*/

        UserExpresspayCardDetail userExpresspayCardDetail = userExpresspayCardDetailService.deposit(uid, pointEAmount);
        UserExpresspayCardDetailPojo userExpresspayCardDetailPojo = new UserExpresspayCardDetailPojo();
        MergeUtil.merge(userExpresspayCardDetail,userExpresspayCardDetailPojo);

        return userExpresspayCardDetailPojo;
    }


    /**
     * 获取用户的圈存总额
     * @param uid
     * @return
     */
    @Auth(entity = UserExpresspayCardDetail.class,parameter = "uid")
    @RequestMapping(value = "/money",method = RequestMethod.GET)
    @ResponseBody
    public Object getUserExpresspayCardDetailMoneyCout(Long uid){

        return ImmutableMap.of("result", userExpresspayCardDetailService.getDepositMoneyCount(uid));
    }
}

