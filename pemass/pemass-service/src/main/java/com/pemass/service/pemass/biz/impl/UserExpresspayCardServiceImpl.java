/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.biz.impl;
import java.util.List;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.persist.dao.biz.UserExpresspayCardDao;
import com.pemass.persist.domain.jpa.biz.UserExpresspayCard;
import com.pemass.persist.enumeration.SequenceEnum;
import com.pemass.pojo.webService.ExpresspayCardBodyPojo;
import com.pemass.pojo.webService.ExpresspayCardHeadPojo;
import com.pemass.pojo.webService.ExpresspayCardPojo;
import com.pemass.service.exception.BizError;
import com.pemass.service.pemass.bas.SequenceService;
import com.pemass.service.pemass.biz.UserExpresspayCardService;
import com.pemass.web.service.client.expresspay.AccountInformation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Description: UserPresspayCardServiceImpl
 * @Author: lin.shi
 * @CreateTime: 2015-08-26 17:13
 */
@Service
public class UserExpresspayCardServiceImpl implements UserExpresspayCardService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private UserExpresspayCardDao userExpresspayCardDao;

    @Resource
    private SequenceService sequenceService;


    @Override
    public UserExpresspayCard getByUserId(Long userId) {
        Expression userPression = new Expression("userId", Operation.Equal,userId);
        List<UserExpresspayCard> list= jpaBaseDao.getEntitiesByExpression(UserExpresspayCard.class, userPression);
        return (list == null || list.size() <=0)?null:list.get(0);
    }

    @Override
    @Transactional
    public UserExpresspayCard bindExpresspayCard(Long userId, String cardNumber) {
        if(this.getByUserId(userId) != null){
            throw new BaseBusinessException(BizError.USER_HAS_CARD);
        }
        if(this.checkExpresspayCardIsBind(cardNumber)){
            throw  new BaseBusinessException(BizError.CARD_IS_BIND);
        }

/*

        //调用中银通接口验证卡号合法性
        ExpresspayCardBodyPojo expresspayCardBodyPojo = new ExpresspayCardBodyPojo();
        expresspayCardBodyPojo.setCardno(cardNumber);
        ExpresspayCardHeadPojo expresspayCardHeadPojo = new ExpresspayCardHeadPojo();

        expresspayCardHeadPojo.setMerchantSeq(sequenceService.obtainSequence(SequenceEnum.OPERATE));
        ExpresspayCardPojo expresspayCardPojo = new ExpresspayCardPojo();

        AccountInformation accountInformation = new AccountInformation();
        expresspayCardPojo = accountInformation.execute(new ExpresspayCardPojo(expresspayCardHeadPojo, expresspayCardBodyPojo));

*/


        UserExpresspayCard expresspayCard = new UserExpresspayCard();
        expresspayCard.setUserId(userId);
        expresspayCard.setCardNumber(cardNumber);
        jpaBaseDao.persist(expresspayCard);
        return expresspayCard;
    }

    @Override
    @Transactional
    public boolean unBindExpresspayCard(Long userId, String cardNumber) {

        return userExpresspayCardDao.unBindUserExpresspayCard(userId,cardNumber);
    }


    /**
     * 检查银通卡是否已被绑定
     * @param cardNumber 银通卡号
     * @return  已被绑定：true, 未被绑定：false
     */
    private boolean  checkExpresspayCardIsBind(String cardNumber){
        Expression cardNumberExpression = new Expression("cardNumber", Operation.Equal,cardNumber);
        List<UserExpresspayCard> list= jpaBaseDao.getEntitiesByExpression(UserExpresspayCard.class, cardNumberExpression);
        return (list==null || list.size() <=0 )? false:true;
    }
}