package com.pemass.service.pemass.biz.impl;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.ArithmeticUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.dao.biz.UserExpresspayCardDetailDao;
import com.pemass.persist.domain.jpa.biz.UserExpresspayCard;
import com.pemass.persist.domain.jpa.biz.UserExpresspayCardDetail;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.enumeration.ConfigEnum;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.DepositStatusEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.persist.enumeration.SequenceEnum;
import com.pemass.pojo.webService.ExpresspayCardBodyPojo;
import com.pemass.pojo.webService.ExpresspayCardHeadPojo;
import com.pemass.pojo.webService.ExpresspayCardPojo;
import com.pemass.service.exception.BizError;
import com.pemass.service.pemass.bas.SequenceService;
import com.pemass.service.pemass.biz.UserExpresspayCardDetailService;
import com.pemass.service.pemass.biz.UserExpresspayCardService;
import com.pemass.service.pemass.poi.UserPointDetailService;
import com.pemass.service.pemass.sys.ConfigService;
import com.pemass.service.pemass.sys.UserService;
import com.pemass.web.service.client.expresspay.RechangeCardAccount;
import com.pemass.web.service.constant.ExpresspayConst;
import com.pemass.web.service.proxy.expresspay.query.TransInnerErrorException_Exception;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: UserExpresspayCardDetailServiceImpl
 * @Author: pokl.huang
 * @CreateTime: 2015-08-26 17:20
 */
@Service
public class UserExpresspayCardDetailServiceImpl implements UserExpresspayCardDetailService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private UserPointDetailService userPointDetailService;

    @Resource
    private UserService userService;

    @Resource
    private ConfigService configService;

    @Resource
    private UserExpresspayCardService userExpresspayCardService;

    @Resource
    private SequenceService sequenceService;


    @Resource
    private UserExpresspayCardDetailDao userExpresspayCardDetailDao;

    @Override
    public DomainPage selectDetailByUid(Long uid, Long pageIndex, Long pageSize) {
        Map map = new HashMap();
        map.put("userId", uid);

        return jpaBaseDao.getEntitiesPagesByFieldList(UserExpresspayCardDetail.class, map, "depositTime", BaseDao.OrderBy.DESC, pageIndex, pageSize);
    }


    @Transactional
    @Override
    public UserExpresspayCardDetail deposit(Long uid, Integer pointEAmount) {

        /**生成明细**/
        UserExpresspayCardDetail userExpresspayCardDetail = this.insertDetail(uid,pointEAmount);




/*

        //调用中银通接口充值
        ExpresspayCardBodyPojo expresspayCardBodyPojo = new ExpresspayCardBodyPojo();
        expresspayCardBodyPojo.setCardno(userExpresspayCardDetail.getCardNumber());

        Integer amount = ArithmeticUtil.floor(userExpresspayCardDetail.getMoney())*100;

        expresspayCardBodyPojo.setAmount(StringUtils.leftPad(amount.toString(),12,"0"));


        ExpresspayCardHeadPojo expresspayCardHeadPojo = new ExpresspayCardHeadPojo();
        expresspayCardHeadPojo.setMerchantSeq(userExpresspayCardDetail.getOperateIdentifier());

        try {
            RechangeCardAccount rechangeCardAccount = new RechangeCardAccount();
            ExpresspayCardPojo expresspayCardPojo = rechangeCardAccount.execute(new ExpresspayCardPojo(expresspayCardHeadPojo, expresspayCardBodyPojo));

            if(expresspayCardPojo.getExpresspayCardHeadPojo().getTxnCode().equals(ExpresspayConst.SUCCESS_CODE)) {
                userExpresspayCardDetail.setDepositStatus(DepositStatusEnum.SUCCEED_OPERATE);
            }else if(expresspayCardPojo.getExpresspayCardHeadPojo().getTxnCode().equals(ExpresspayConst.OPERATEING_CODE)){
                userExpresspayCardDetail.setDepositStatus(DepositStatusEnum.OPERATEING);
            }else {
                userExpresspayCardDetail.setDepositStatus(DepositStatusEnum.FAILURE_OPERATE);
            }
            userExpresspayCardDetail.setMessage(expresspayCardPojo.getExpresspayCardHeadPojo().getErrMsg());
        }catch (TransInnerErrorException_Exception e) {
            userExpresspayCardDetail.setMessage(e.getCause().getMessage());
            userExpresspayCardDetail.setDepositStatus(DepositStatusEnum.FAILURE_OPERATE);
        } catch (Exception e) {
            userExpresspayCardDetail.setDepositStatus(DepositStatusEnum.FAILURE_OPERATE);
            userExpresspayCardDetail.setMessage(e.getMessage());
        }
        jpaBaseDao.merge(userExpresspayCardDetail);

*/


        /**抵扣用户E通币**/
        User user = userService.getById(uid);
        userPointDetailService.deductionPoint(user, ConsumeTypeEnum.EXPRESSPAYCARD_DEPOSIT, userExpresspayCardDetail.getId(), PointTypeEnum.E,pointEAmount,null);

        return userExpresspayCardDetail;
    }

    @Override
    public UserExpresspayCardDetail insertDetail(Long uid, Integer pointEAmount) {
        UserExpresspayCardDetail userExpresspayCardDetail = new UserExpresspayCardDetail();
        UserExpresspayCard userExpresspayCard = userExpresspayCardService.getByUserId(uid);
        if (userExpresspayCard == null){
            throw new BaseBusinessException(BizError.USER_NO_CARD);
        }
        userExpresspayCardDetail.setCardNumber(userExpresspayCard.getCardNumber());
        userExpresspayCardDetail.setDepositStatus(DepositStatusEnum.OPERATEING);
        userExpresspayCardDetail.setDepositTime(new Date());
        userExpresspayCardDetail.setPointEAmount(pointEAmount);
        userExpresspayCardDetail.setOperateIdentifier(sequenceService.obtainSequence(SequenceEnum.OPERATE));
        /**计算手续费**/
        Config config = configService.getConfigByKey(ConfigEnum.DEPOSIR_POUNDAGE);
        Float poundage_rate  = Float.valueOf(config.getValue());

        userExpresspayCardDetail.setPoundage(ArithmeticUtil.mul(pointEAmount,poundage_rate));
        userExpresspayCardDetail.setMoney(pointEAmount - userExpresspayCardDetail.getPoundage());
        userExpresspayCardDetail.setUserId(uid);

        jpaBaseDao.persist(userExpresspayCardDetail);
        return userExpresspayCardDetail;
    }

    @Override
    public Float getDepositMoneyCount(Long uid) {
        Object[] objects = userExpresspayCardDetailDao.getDepositMoneyCount(uid);

        return (objects != null && objects.length>0) ? Float.valueOf(objects[0].toString()) : 0;
    }
}

