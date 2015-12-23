/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.biz.impl;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.dao.biz.ExpresspayCardDao;
import com.pemass.persist.domain.jpa.biz.ExpresspayCard;
import com.pemass.persist.domain.jpa.biz.ExpresspayCardDetail;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.enumeration.CardVarietyEnum;
import com.pemass.persist.enumeration.ConfigEnum;
import com.pemass.persist.enumeration.LogisticsStatusEnum;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.persist.enumeration.PutawayStatusEnum;
import com.pemass.persist.enumeration.PutawayStyleEnum;
import com.pemass.persist.enumeration.SequenceEnum;
import com.pemass.service.exception.BizError;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.bas.SequenceService;
import com.pemass.service.pemass.biz.ExpresspayCardService;
import com.pemass.service.pemass.sys.ConfigService;
import com.pemass.service.pemass.sys.TerminalUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: ExpresspayCardServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2015-04-16 16:59
 */
@Service
public class ExpresspayCardServiceImpl implements ExpresspayCardService {

    @Resource
    private ExpresspayCardDao expresspayCardDao;

    @Resource
    private TerminalUserService terminalUserService;

    @Resource
    private ConfigService configService;

    @Resource
    private SequenceService sequenceService;

    @Override
    public List<String> add(Long terminalUserId, String[] cardNumber, String[] cardCategory) {
        TerminalUser terminalUser = terminalUserService.getTerminalById(terminalUserId);
        //判断权限
        Config config = configService.getConfigByKey(ConfigEnum.JFT_ORGANIZATION_ID);
        if (Long.valueOf(config.getValue()) != terminalUser.getOrganizationId()) {
            throw new BaseBusinessException(SysError.ORGANIZATIONID_IS_NOT_AUTHORITY);
        }
        List<String> list = new ArrayList<String>();
        String cardIdntifier = cardIdntifier();
        for (int i = 0; i < cardNumber.length; i++) {
            ExpresspayCard isExpresspayCard = expresspayCardDao.getEntityByField(ExpresspayCard.class, "cardNumber", cardNumber[i]);
            if (null == isExpresspayCard) {
                ExpresspayCard expresspayCard = new ExpresspayCard();
                expresspayCard.setCardCategory(CardVarietyEnum.valueOf(CardVarietyEnum.class, cardCategory[i]));
                expresspayCard.setShippingStatus(LogisticsStatusEnum.PUTAWAY);
                expresspayCard.setCardNumber(cardNumber[i]);
                if (expresspayCard.getCardCategory().equals(CardVarietyEnum.CHIP_CARD)) {
                    expresspayCard.setCostPrice(10D);
                } else if (expresspayCard.getCardCategory().equals(CardVarietyEnum.DISK_CARD)) {
                    expresspayCard.setCostPrice(5D);
                }
                expresspayCard.setOrganizationId(terminalUser.getOrganizationId());
                expresspayCard.setSourceId(terminalUser.getOrganizationId());
                expresspayCard.setUseStatus(OrderItemStatusEnum.UNUSED);
                expresspayCard.setCardIdentifier(cardIdntifier);
                expresspayCard.setPutawayStyleEnum(PutawayStyleEnum.SCAN);
                expresspayCardDao.persist(expresspayCard);
            } else {
                if (isExpresspayCard != null) {//判断此卡是否可录入
                    list.add(isExpresspayCard.getCardNumber());
                }
            }
        }
        return list;
    }

    @Override
    public List<String> adds(Long terminalUserId, String beginNumber, String endNumber, CardVarietyEnum
            cardVarietyEnum) {
        TerminalUser terminalUser = terminalUserService.getTerminalById(terminalUserId);
        //判断权限
        Config config = configService.getConfigByKey(ConfigEnum.JFT_ORGANIZATION_ID);
        if (Long.valueOf(config.getValue()) !=terminalUser.getOrganizationId()) {
            throw new BaseBusinessException(SysError.ORGANIZATIONID_IS_NOT_AUTHORITY);
        }
        List<String> list = new ArrayList<String>();
        long begin = Long.valueOf(beginNumber);
        long end = Long.valueOf(endNumber);
        String cardIdntifier = cardIdntifier();
        for (long i = begin; i <= end; i++) {
            ExpresspayCard isExpresspayCard = expresspayCardDao.getEntityByField(ExpresspayCard.class, "cardNumber", String.valueOf(i));
            if (null == isExpresspayCard) {
                ExpresspayCard expresspayCard = new ExpresspayCard();
                expresspayCard.setCardCategory(cardVarietyEnum);
                expresspayCard.setShippingStatus(LogisticsStatusEnum.PUTAWAY);
                expresspayCard.setCardNumber(String.valueOf(i));
                if (cardVarietyEnum.equals(CardVarietyEnum.CHIP_CARD)) {
                    expresspayCard.setCostPrice(10D);
                } else if (cardVarietyEnum.equals(CardVarietyEnum.DISK_CARD)) {
                    expresspayCard.setCostPrice(5D);
                }
                expresspayCard.setOrganizationId(terminalUser.getOrganizationId());
                expresspayCard.setSourceId(terminalUser.getOrganizationId());
                expresspayCard.setUseStatus(OrderItemStatusEnum.UNUSED);
                expresspayCard.setCardIdentifier(cardIdntifier);
                expresspayCard.setPutawayStyleEnum(PutawayStyleEnum.BATCH_QUANTITY);
                expresspayCardDao.persist(expresspayCard);
            } else {
                if (isExpresspayCard != null) {//判断此卡是否可录入
                    list.add(isExpresspayCard.getCardNumber());
                }
            }
        }
        return list;
    }

    @Override
    public ExpresspayCard getById(String cardNumber, Long terminalUserId) {
        TerminalUser terminalUser = terminalUserService.getTerminalById(terminalUserId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cardNumber", cardNumber);
        map.put("organizationId", terminalUser.getOrganizationId());
        map.put("useStatus", OrderItemStatusEnum.UNUSED);
        map.put("shippingStatus", LogisticsStatusEnum.PUTAWAY);
        List<ExpresspayCard> list = expresspayCardDao.getEntitiesByFieldList(ExpresspayCard.class, map);
        if (list.size() <= 0) {
            throw new BaseBusinessException(BizError.CARDNUMBER_NOT_EXIST);
        }
        return list.get(0);
    }

    @Override
    public Boolean updateAllot(String[] cardNumber, Long organizationId, Long terminalUserId) {
        TerminalUser terminalUser = terminalUserService.getTerminalById(terminalUserId);
        Organization organization = expresspayCardDao.getEntityById(Organization.class, organizationId);
        if (organization == null) {
            throw new BaseBusinessException(SysError.ORGANIZATIONID_NOT_EXIST);
        }
        String cardIdntifier = cardIdntifier();
        for (String str : cardNumber) {
            ExpresspayCard expresspayCard = expresspayCardDao.getEntityByid(str, terminalUser.getOrganizationId());
            if (expresspayCard == null) {
                throw new BaseBusinessException(BizError.CARD_NOT_ALLOT);
            }

            ExpresspayCardDetail expresspayCardDetail = new ExpresspayCardDetail();
            expresspayCardDetail.setCardNumber(expresspayCard.getCardNumber());
            expresspayCardDetail.setSourceId(expresspayCard.getOrganizationId());
            expresspayCardDetail.setOrganizationId(organization.getId());
            expresspayCardDetail.setCardIdentifier(cardIdntifier);
            expresspayCardDetail.setCardCategory(expresspayCard.getCardCategory());
            expresspayCardDetail.setPutawayStyleEnum(PutawayStyleEnum.SCAN);
            expresspayCardDetail.setPutawayStatusEnum(PutawayStatusEnum.INTRANSIT);
            expresspayCardDetail.setCashierUserId(terminalUser.getId());
            expresspayCardDao.persist(expresspayCardDetail);


            expresspayCard.setCardIdentifier(cardIdntifier);
            expresspayCard.setSourceId(expresspayCard.getOrganizationId());
            expresspayCard.setOrganizationId(organization.getId());
            expresspayCard.setShippingStatus(LogisticsStatusEnum.ALLOT);
            expresspayCard.setPutawayStyleEnum(PutawayStyleEnum.SCAN);
            expresspayCardDao.merge(expresspayCard);
        }
        return true;
    }

    @Override
    public Boolean updateAllots(String beginNumber, String endNumber, Long organizationId, Long terminalUserId) {
        TerminalUser terminalUser = terminalUserService.getTerminalById(terminalUserId);
        Organization organization = expresspayCardDao.getEntityById(Organization.class, organizationId);
        if (organization == null) {
            throw new BaseBusinessException(SysError.ORGANIZATIONID_NOT_EXIST);
        }
        String cardIdntifier = cardIdntifier();
        long begin = Long.valueOf(beginNumber);
        long end = Long.valueOf(endNumber);
        int count = 0;
        for (long i = begin; i <= end; i++) {
            ExpresspayCard expresspayCard = expresspayCardDao.getEntityByid(String.valueOf(i), terminalUser.getOrganizationId());
            if (expresspayCard == null) {
                throw new BaseBusinessException(BizError.CARD_NOT_ALLOT);
            }
            ExpresspayCardDetail expresspayCardDetail = new ExpresspayCardDetail();
            expresspayCardDetail.setCardNumber(String.valueOf(i));
            expresspayCardDetail.setSourceId(terminalUser.getOrganizationId());
            expresspayCardDetail.setOrganizationId(organization.getId());
            expresspayCardDetail.setCardIdentifier(cardIdntifier);
            expresspayCardDetail.setCardCategory(expresspayCard.getCardCategory());
            expresspayCardDetail.setPutawayStyleEnum(PutawayStyleEnum.BATCH_QUANTITY);
            expresspayCardDetail.setPutawayStatusEnum(PutawayStatusEnum.INTRANSIT);
            expresspayCardDetail.setCashierUserId(terminalUser.getId());
            expresspayCardDao.persist(expresspayCardDetail);
            ++count;
        }
        int updateCount = expresspayCardDao.updateAllots(terminalUser, organization.getId(), beginNumber, endNumber, cardIdntifier);
        if (count != updateCount) {
            throw new BaseBusinessException(BizError.CARD_NOT_ALLOT);
        }
        return true;
    }

    @Override
    public Long onSaleCount(Long terminalUserId, OrderItemStatusEnum statusEnum, LogisticsStatusEnum
            logisticsStatusEnum) {
        TerminalUser terminalUser = terminalUserService.getTerminalById(terminalUserId);
        return expresspayCardDao.onSaleCount(terminalUser.getOrganizationId(), statusEnum, logisticsStatusEnum);
    }

    @Override
    public DomainPage onsaleSearch(Long terminalUserId, Long pageIndex, Long pageSize) {
        TerminalUser terminalUser = terminalUserService.getTerminalById(terminalUserId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("organizationId", terminalUser.getOrganizationId());
        map.put("useStatus", OrderItemStatusEnum.UNUSED);
        map.put("shippingStatus", LogisticsStatusEnum.PUTAWAY);
        return expresspayCardDao.getEntitiesPagesByFieldList(ExpresspayCard.class, map, "cardNumber", BaseDao.OrderBy.ASC, pageIndex, pageSize);
    }

    @Override
    public DomainPage soldSearch(Long terminalUserId, String cardNumber, long pageIndex, long pageSize) {
        TerminalUser terminalUser = terminalUserService.getTerminalById(terminalUserId);
        return expresspayCardDao.getBySoldSearch(terminalUser.getOrganizationId(),cardNumber, pageIndex, pageSize);
    }


    @Override
    public List<Object[]> getBycardIdentifier(String identifier) {
        return expresspayCardDao.getBycardIdentifier(identifier);
    }

    @Override
    public boolean updatePutaways(Long organizationId, String cardIdntifier) {

        return expresspayCardDao.updatePutaways(organizationId, cardIdntifier);
    }

    @Override
    public ExpresspayCard getById(Long expresspayCardId) {
        return expresspayCardDao.getEntityById(ExpresspayCard.class, expresspayCardId);
    }


    private String cardIdntifier() {
        return sequenceService.obtainSequence(SequenceEnum.EXPRESSPAY_CARD);
    }
}