/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.biz.impl;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.dao.biz.ExpresspayCardRecordDao;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.persist.domain.jpa.biz.ExpresspayCard;
import com.pemass.persist.domain.jpa.biz.ExpresspayCardRecord;
import com.pemass.persist.domain.jpa.ec.Invoice;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.persist.enumeration.SmsTypeEnum;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.exception.BizError;
import com.pemass.service.pemass.bas.SmsMessageService;
import com.pemass.service.pemass.biz.ExpresspayCardRecordService;
import com.pemass.service.pemass.sys.AuthService;
import com.pemass.service.pemass.sys.TerminalUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: ExpresspayCardRecordServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2015-04-18 15:33
 */
@Service
public class ExpresspayCardRecordServiceImpl implements ExpresspayCardRecordService {

    @Resource
    private ExpresspayCardRecordDao expresspayCardRecordDao;

    @Resource
    private TerminalUserService terminalUserService;

    @Resource
    private AuthService authService;

    @Resource
    private SmsMessageService smsMessageService;

    @Override
    public Boolean add(ExpresspayCardRecord expresspayCardRecord, Invoice invoice, String cardNumber, Long terminalUserId) {

        TerminalUser terminalUser = terminalUserService.getTerminalById(terminalUserId);
        ExpresspayCard expresspayCard = expresspayCardRecordDao.findEntity(cardNumber, terminalUser.getOrganizationId(), OrderItemStatusEnum.UNUSED);

        if (expresspayCard == null) {
            throw new BaseBusinessException(BizError.CARD_IS_USED);
        }
        boolean isRegister = false;
        User user = expresspayCardRecordDao.getEntityByField(User.class, "username", expresspayCardRecord.getTelephone());
        if (user == null) {
            user = new User();
            user.setUsername(expresspayCardRecord.getTelephone());
            user.setPassword(expresspayCardRecord.getTelephone());
            authService.register(user);
            isRegister = true;
        }

        if (null != invoice.getReceiveName()) {
            expresspayCardRecordDao.persist(invoice);
            expresspayCardRecord.setInvoiceId(invoice.getId());
        }
        expresspayCard.setUserId(user.getId());
        expresspayCard.setUseStatus(OrderItemStatusEnum.USED);
        expresspayCardRecord.setCashierUserId(terminalUser.getId());
        expresspayCardRecord.setExpresspayCardId(expresspayCard.getId());
        expresspayCardRecordDao.persist(expresspayCardRecord);
        if (isRegister) {
            smsMessageService.append(user.getUsername(), SmsTypeEnum.SYS_REG, new String[]{PemassConst.CLOUDMONEY_LATEST_URL});
        }
        return true;
    }

    @Override
    public DomainPage findByCondition(ExpresspayCard expresspayCard, Integer invoice, Date startDate, Date endDate,
                                      Float startMoney, Float endMoney, Province provinceId, Organization organizationId,
                                      DomainPage domainPage, String ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> conMap = new HashMap<String, Object>();

        if (startMoney != null && startMoney >= 0) {
            map.put("r.total_price>=", startMoney);
        }
        if (endMoney != null && endMoney > 0) {
            map.put("r.total_price<=", endMoney);
        }
        if (expresspayCard != null) {
            if (expresspayCard.getCardNumber() != null &&
                    !"".equals(expresspayCard.getCardNumber())) {
                map.put("e.card_number=", expresspayCard.getCardNumber());
            }
            if (expresspayCard.getCardCategory() != null) {
                map.put("e.card_category=", expresspayCard.getCardCategory().toString());
            }
        }
        if (invoice != null && invoice == 1) {
            conMap.put("r.invoice_id", "is not null ");
        }
        if (invoice != null && invoice == 0) {
            conMap.put("r.invoice_id", "is  null ");
        }
        if (!"".equals(ids)) {
            conMap.put(" o.id in", "(" + ids + ")");
        }
        if (startDate != null) {
            map.put("r.create_time >=", startDate);
        }
        if (endDate != null) {
            map.put("r.create_time <=", endDate);
        }
        if (provinceId != null) {
            map.put("o.province_id=", provinceId.getId());
        }
        if (organizationId != null) {
            map.put("o.id=", organizationId.getId());
        }

        return expresspayCardRecordDao.findByCondition(map, conMap, domainPage.getPageIndex(), domainPage.getPageSize());
    }
}