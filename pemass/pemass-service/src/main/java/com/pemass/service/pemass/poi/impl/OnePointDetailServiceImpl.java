/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;


import com.google.common.collect.ImmutableList;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.dao.poi.OnePointDetailDao;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.poi.OnePointConsumeDetail;
import com.pemass.persist.domain.jpa.poi.OnePointDetail;
import com.pemass.persist.domain.jpa.sys.CreditRecord;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.enumeration.PayStatusEnum;
import com.pemass.persist.enumeration.PointChannelEnum;
import com.pemass.persist.enumeration.SmsTypeEnum;
import com.pemass.pojo.poi.OrderPointPayDetailPojo;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.exception.PoiError;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.bas.SmsMessageService;
import com.pemass.service.pemass.poi.OnePointConsumeDetailService;
import com.pemass.service.pemass.poi.OnePointDetailService;
import com.pemass.service.pemass.sys.AuthService;
import com.pemass.service.pemass.sys.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: OnePointDetailServiceImpl
 * @Author: lin.shi
 * @CreateTime: 2015-06-11 21:31
 */
@Service
public class OnePointDetailServiceImpl implements OnePointDetailService {
    @Resource
    private OnePointDetailDao onePointDetailDao;

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private UserService userService;

    @Resource
    private AuthService authService;

    @Resource
    private SmsMessageService smsMessageService;

    @Resource
    private OnePointConsumeDetailService onePointConsumeDetailService;


    @Override
    public DomainPage getPointDetail(Long uid, Long pageIndex, Long pageSize) {
        return onePointDetailDao.getPointDetail(uid, pageIndex, pageSize);
    }

    @Transactional
    @Override
    public boolean givePoint(Long uid, String givingUsername, Integer amount) {
        /**1,判断是否添加C端用户*/
        User givingUser = jpaBaseDao.getEntityByField(User.class, "username", givingUsername);
        boolean isRegister = false;
        if (givingUser == null) {
            givingUser = new User();
            givingUser.setUsername(givingUsername);
            givingUser.setPassword(givingUsername);
            givingUser = authService.register(givingUser);
            isRegister = true;
        }
        User user = jpaBaseDao.getEntityById(User.class, uid);
        if (givingUser.getUsername().equals(user.getUsername())) {
            throw new BaseBusinessException(PoiError.USER_IS_NO_GIVE);
        }
        List<OnePointDetail> list = onePointDetailDao.selectDetailByUserId(uid);
        for (OnePointDetail detail : list) {
            if (amount <= detail.getUseableAmount()) {
                insertOnePointDetail(detail, amount, givingUser);
                detail.setUseableAmount(detail.getUseableAmount() - amount);
                jpaBaseDao.merge(detail);
                amount = 0;
                break;
            } else {
                Integer newAmount = amount - detail.getUseableAmount();
                amount = newAmount;
                insertOnePointDetail(detail, detail.getUseableAmount(), givingUser);
                detail.setUseableAmount(0);
                jpaBaseDao.merge(detail);
            }
        }
        if (amount > 0) {
            throw new BaseBusinessException(PoiError.ONEPOINTDETAIL_IS_LACKING);
        }

        if (isRegister) {
            smsMessageService.append(givingUser.getUsername(), SmsTypeEnum.SYS_REG, new String[]{PemassConst.CLOUDMONEY_LATEST_URL});
        }

        return true;
    }


    private void insertOnePointDetail(OnePointDetail detail, Integer amount, User user) {
        OnePointDetail pointDetail = new OnePointDetail();
        pointDetail.setUseableAmount(amount);
        pointDetail.setAmount(amount);
        pointDetail.setUserId(user.getId());
        pointDetail.setExpiryTime(detail.getExpiryTime());
        pointDetail.setBelongUserId(detail.getUserId());
        pointDetail.setPointChannelEnum(PointChannelEnum.ONE_POINT_GIVE);
        pointDetail.setOrderId(detail.getOrderId());
        pointDetail.setIsClear(false);
        onePointDetailDao.persist(pointDetail);
    }


    @Override
    public Long selectPointGivingAmount(long userId, boolean isGive) {
        return onePointDetailDao.selectPointGiveAmount(userId, isGive);
    }

    @Override
    public DomainPage selectPointGiveDetailAmount(long uid, long pageSize, long pageIndex) {
        return onePointDetailDao.selectPointGiveDetailAmount(uid, pageSize, pageIndex);
    }

    @Override
    public Long getByUseableAmount(Long uid) {
        return onePointDetailDao.getByUseableAmount(uid);
    }

    @Override
    public List<Object[]> getByOnePointSearch(String username) {
        User user = onePointDetailDao.getEntityByField(User.class, "username", username);
        List<Object[]> list = onePointDetailDao.getByOnePointSearch(user.getId());
        List<Object[]> newList = new ArrayList<Object[]>();
        List<Object[]> userList = new ArrayList<Object[]>();
        for (Object[] objects : list) {
            if (user.getUsername().equals(objects[2].toString())) {
                userList.add(objects);
            } else {
                newList.add(objects);
            }
        }
        if (userList != null && userList.size() > 0) {
            newList.add(0, userList.get(0));
        }
        return newList;
    }

    @Override
    public Long getByRemainPointCount(String username) {
        User user = userService.getByUsername(username);
        return onePointDetailDao.getByRemainpointCount(user.getId());
    }

    @Override
    public Long getByRemainPointCount(Long uid) {
        return onePointDetailDao.getByRemainpointCount(uid);
    }

    @Override
    public Long getByGivePointCount(String username) {
        User user = userService.getByUsername(username);
        return onePointDetailDao.getByGivePointCount(user.getId());
    }

    @Override
    public OnePointDetail insert(OnePointDetail onePointDetail) {
        jpaBaseDao.persist(onePointDetail);
        return onePointDetail;
    }


    @Override
    public DomainPage pointGiveSearch(Long uid,Boolean isGive, Long pageIndex, Long pageSize) {
        return onePointDetailDao.pointGiveSearch(uid, isGive, pageIndex, pageSize);
    }

    @Transactional
    @Override
    public boolean pointRecycle(Long pointDetailId) {
        OnePointDetail onePointDetail = jpaBaseDao.getEntityById(OnePointDetail.class, pointDetailId);
        if (onePointDetail == null) {
            throw new BaseBusinessException(PoiError.ONEPOINTDETAIL_NOT_FOUND);
        }
        onePointDetail.setUserId(onePointDetail.getBelongUserId());
        onePointDetail.setPointChannelEnum(PointChannelEnum.ONE_POINT_RECOVERY);
        jpaBaseDao.merge(onePointDetail);
        return true;
    }

    @Transactional
    @Override
    public Boolean pointDeduct(OrderPointPayDetailPojo orderPointPayDetailPojo){
         List<OnePointDetail> list = onePointDetailDao.selectDetail(orderPointPayDetailPojo.getUserId(), orderPointPayDetailPojo.getBelongUserId());//getEntitiesByExpressionList(OnePointDetail.class, params);

        if(list == null || list.size() <=0){
            throw new BaseBusinessException(PoiError.ONEPOINTDETAIL_NOT_FOUND);
        }
        Integer amount = orderPointPayDetailPojo.getAmount();
        Order order  = jpaBaseDao.getEntityById(Order.class,orderPointPayDetailPojo.getOrderId());

        for(OnePointDetail detail : list){
            if(amount >= detail.getUseableAmount()){
                amount -= detail.getUseableAmount();
                onePointConsumeDetailService.insertDetail(detail,order,detail.getUseableAmount());
                detail.setUseableAmount(0);
            }else{
                Integer temp = 0;
                onePointConsumeDetailService.insertDetail(detail,order,amount);
                temp = detail.getUseableAmount();
                detail.setUseableAmount(detail.getUseableAmount() - amount);
                amount -= temp;
            }
            onePointDetailDao.merge(detail);

            if(amount <= 0) break;
        }
        if(amount >0){
            throw new BaseBusinessException(PoiError.AMOUNT_NOT_ENOUGH);
        }
        return true;
    }

    @Transactional
    @Override
    public void usePointReturn(Order order) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orderId", order.getId());

        List<OnePointConsumeDetail> onePointConsumeDetails = jpaBaseDao.getEntitiesByFieldList(OnePointConsumeDetail.class, param);

        for (OnePointConsumeDetail onePointConsumeDetail : onePointConsumeDetails) {
            OnePointDetail onePointDetail = jpaBaseDao.getEntityById(OnePointDetail.class, onePointConsumeDetail.getOnePointDetailId());
            if (onePointDetail.getExpiryTime().after(new Date())) {//归还未过期积分
                onePointDetail.setUseableAmount(onePointConsumeDetail.getPayableAmount() + onePointDetail.getUseableAmount());
                jpaBaseDao.merge(onePointDetail);
            }
            /**将消耗明细逻辑删除**/
            onePointConsumeDetail.setAvailable(AvailableEnum.UNAVAILABLE);
            jpaBaseDao.merge(onePointConsumeDetail);
        }
    }

    @Transactional
    @Override
    public void clearPoint(Long uid) {
        onePointDetailDao.clearPoint(uid);
    }

    @Override
    @Transactional
    public Boolean repayment(Long uid) {
        Expression userExpression = new Expression("userId", Operation.Equal,uid);
        Expression isValidExpression = new Expression("isValid", Operation.Equal,Integer.valueOf(1));
        List<CreditRecord> creditRecords = jpaBaseDao.getEntitiesByExpressionList(CreditRecord.class, ImmutableList.of(isValidExpression,userExpression));
        if(creditRecords == null || creditRecords.size() <=0){
            throw new BaseBusinessException(SysError.USER_NOT_CREDIT);
        }
        CreditRecord creditRecord = creditRecords.get(0);
        creditRecord.setPayStatus(PayStatusEnum.HAS_PAY);
        jpaBaseDao.merge(creditRecord);
        //清理壹购积分
        this.clearPoint(uid);
        return true;
    }

}