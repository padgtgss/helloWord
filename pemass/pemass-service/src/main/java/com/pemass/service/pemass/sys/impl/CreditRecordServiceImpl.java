/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.persist.domain.jpa.sys.CreditRecord;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PayStatusEnum;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.sys.CreditRecordService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description: CreditRecordServiceImpl
 * @Author: lin.shi
 * @CreateTime: 2015-08-05 19:44
 */
@Service
public class CreditRecordServiceImpl implements CreditRecordService {

    @Resource
    private BaseDao jpaBaseDao;



    @Transactional
    @Override
    public void applyCredit(CreditRecord creditRecord) {
        if(this.getByUserId(creditRecord.getUserId()) != null){
            throw  new BaseBusinessException(SysError.USER_HAS_CREDIT);
        }
        creditRecord.setAuditStatus(AuditStatusEnum.HAS_AUDIT);
        creditRecord.setPayStatus(PayStatusEnum.NONE_PAY);
        creditRecord.setIsValid(1);

        creditRecord.setStartTime(new Date());
        creditRecord.setEndTime(DateTime.now().plusMonths(12).toDate());
        creditRecord.setCreditLimit(Double.valueOf(50000));
        creditRecord.setUseableCreditLimit(Double.valueOf(50000));

        jpaBaseDao.persist(creditRecord);

        User user = jpaBaseDao.getEntityById(User.class,creditRecord.getUserId());
        user.setIsCredited(1);
        jpaBaseDao.merge(user);
    }

    @Override
    public CreditRecord getByUserId(Long userId) {
        Preconditions.checkNotNull(userId);

        Expression userIdExpression = new Expression("userId", Operation.Equal, userId);
        Expression isValidExpression = new Expression("isValid", Operation.Equal, Integer.valueOf(1));

        List<CreditRecord> list = jpaBaseDao.getEntitiesByExpressionList(CreditRecord.class, ImmutableList.of(userIdExpression, isValidExpression));
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }
}