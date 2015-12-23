/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.bas;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.sms.SmsMessage;
import com.pemass.persist.enumeration.SmsTypeEnum;

/**
 * @Description: SmsMessageDao
 * @Author: zhou hang
 * @CreateTime: 2015-01-21 10:01
 */
public interface SmsMessageDao extends BaseDao {
    /**
     * 插入短信数据
     * @param smsMessage
     * @return
     */
    boolean insertEntity(SmsMessage smsMessage);

    /**
     * 查询验证数据
     * @param username
     * @param regVal
     * @param validateCode
     * @return
     */
    SmsMessage findEntity(String username, SmsTypeEnum regVal, String validateCode);

    /**
     * 查询验证数据
     * @param username
     * @param customOrderNoreg
     * @param customOrderReged
     * @param validateCode
     * @return
     */
    SmsMessage findEntity(String username, SmsTypeEnum customOrderNoreg, SmsTypeEnum customOrderReged, String validateCode);
}
