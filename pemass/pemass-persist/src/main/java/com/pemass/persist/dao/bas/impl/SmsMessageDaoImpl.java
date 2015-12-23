/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.bas.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.bas.SmsMessageDao;
import com.pemass.persist.domain.jpa.sms.SmsMessage;
import com.pemass.persist.enumeration.SmsTypeEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @Description: SmsMessageDaoImpl
 * @Author: zhou hang
 * @CreateTime: 2015-01-21 10:36
 */
@Repository
public class SmsMessageDaoImpl extends JPABaseDaoImpl implements SmsMessageDao {

    @Override
    public boolean insertEntity(SmsMessage smsMessage) {
        persist(smsMessage);
        return smsMessage.getId() == null;
    }

    @Override
    public SmsMessage findEntity(String username, SmsTypeEnum regVal, String validateCode) {
        StringBuffer sb = new StringBuffer();
        sb.append("from SmsMessage s where  ");
        sb.append(" s.receiveNumber=?1 and s.validateCode=?2 and s.smsType=?3");
        sb.append(" and s.expiryTime >=?4 and s.isUsed=?5 and s.isSuccess=?6 ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, username);
        query.setParameter(2, validateCode);
        query.setParameter(3, regVal);
        query.setParameter(4, new Date());
        query.setParameter(5, 0);
        query.setParameter(6, 1);

        List<SmsMessage> list = query.getResultList();
        if (list != null && list.size() > 0) {
               return list.get(0);
        }
        return null;
    }

    @Override
    public SmsMessage findEntity(String username, SmsTypeEnum customOrderNoreg, SmsTypeEnum customOrderReged, String validateCode) {
        StringBuffer sb = new StringBuffer();
        sb.append("from SmsMessage s where  ");
        sb.append(" s.receiveNumber=?1 and s.validateCode=?2 and (s.smsType=?3 or s.smsType=?4) ");
        sb.append(" and s.expiryTime >=?5 and s.isUsed=?6 and s.isSuccess=?7 ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, username);
        query.setParameter(2, validateCode);
        query.setParameter(3, customOrderNoreg);
        query.setParameter(4, customOrderReged);
        query.setParameter(5, new Date());
        query.setParameter(6, 0);
        query.setParameter(7, 1);

        List<SmsMessage> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}