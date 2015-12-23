/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.biz.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.dao.biz.ExpresspayCardDetailDao;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.persist.domain.jpa.biz.ExpresspayCardDetail;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.enumeration.PutawayStatusEnum;
import com.pemass.service.pemass.biz.ExpresspayCardDetailService;
import com.pemass.service.pemass.biz.ExpresspayCardService;
import com.pemass.service.pemass.sys.TerminalUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: ExpresspayCardDetailServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2015-04-23 11:06
 */
@Service
public class ExpresspayCardDetailServiceImpl implements ExpresspayCardDetailService {

    @Resource
    private ExpresspayCardDetailDao expresspayCardDetailDao;

    @Resource
    private TerminalUserService terminalUserService;

    @Resource
    private ExpresspayCardService expresspayCardService;

    @Override
    public DomainPage allotSearch(Long terminalUserId, PutawayStatusEnum statusEnum, String condition, long pageIndex, long pageSize) {
        TerminalUser terminalUser = terminalUserService.getTerminalById(terminalUserId);
        return expresspayCardDetailDao.allotSearch(terminalUser.getOrganizationId(), statusEnum, condition, pageIndex, pageSize);
    }

    @Override
    public List<Object[]> getBycardIdentifier(String identifier) {
        return expresspayCardDetailDao.getBycardIdentifier(identifier);
    }


    @Override
    public Long allotCount(Long terminalUserId, String company) {
        TerminalUser terminalUser = terminalUserService.getTerminalById(terminalUserId);
        return expresspayCardDetailDao.allotCount(terminalUser.getOrganizationId(), company);
    }

    @Transactional
    @Override
    public boolean validatePutaway(Long terminalUserId, String cardIdntifier) {
        TerminalUser terminalUser = terminalUserService.getTerminalById(terminalUserId);
        boolean isValidate = expresspayCardDetailDao.updatePutaways(terminalUser.getOrganizationId(), cardIdntifier);
        isValidate = expresspayCardService.updatePutaways(terminalUser.getOrganizationId(), cardIdntifier);
        return isValidate;
    }

    @Override
    public DomainPage findByPutawayList(Date startTime, Date endTime, Province province, Organization organization,
                                        ExpresspayCardDetail cardRecord, Long conditionId, DomainPage domainPage,
                                        String conditionp, String conditiond) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (startTime != null) {
            map.put("b.create_time>=", startTime);
        }
        if (endTime != null) {
            map.put("b.create_time<=", endTime);
        }
        if (province != null) {
            map.put(conditiond +".province_id=", province.getId());
        }
        if (organization != null) {
            map.put(conditiond +".id=", organization.getId());
        }
        if (cardRecord != null) {
            map.put("b.putaway_status=", cardRecord.getPutawayStatusEnum().ordinal());
        }
        map.put(conditionp +".id=", conditionId);

        return expresspayCardDetailDao.findByPutawayList(map,conditiond,domainPage.getPageIndex(),domainPage.getPageSize());
    }

}