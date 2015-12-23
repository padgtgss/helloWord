/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.biz;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.DateUtil;
import com.pemass.persist.enumeration.CardVarietyEnum;
import com.pemass.persist.enumeration.PutawayStatusEnum;
import com.pemass.persist.enumeration.PutawayStyleEnum;
import com.pemass.pojo.biz.ExpresspayCardPojo;
import com.pemass.pojo.biz.ExpresspayCardSituationPojo;
import com.pemass.service.pemass.biz.ExpresspayCardDetailService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: ExpresspayCardDetailController
 * @Author: zhou hang
 * @CreateTime: 2015-04-23 11:03
 */
@Controller
@RequestMapping("/expresspayCard/detail")
public class ExpresspayCardDetailController {

    @Resource
    private ExpresspayCardDetailService expresspayCardDetailService;

    /**
     * 查询商家调拨中记录
     *
     * @param terminalUserId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "inTransit/search", method = RequestMethod.GET, params = {"terminalUserId", "pageIndex", "pageSize"})
    @ResponseBody
    public Object inTransit(Long terminalUserId, Long pageIndex, Long pageSize) {
        DomainPage domainPage = expresspayCardDetailService.allotSearch(terminalUserId, PutawayStatusEnum.INTRANSIT, "source_id",
                pageIndex, pageSize);
        return merge(domainPage, PutawayStatusEnum.INTRANSIT);
    }

    /**
     * 查询商家调拨记录
     *
     * @param terminalUserId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "putaway/search", method = RequestMethod.GET, params = {"terminalUserId", "pageIndex", "pageSize"})
    @ResponseBody
    public Object putaway(Long terminalUserId, Long pageIndex, Long pageSize) {
        DomainPage domainPage = expresspayCardDetailService.allotSearch(terminalUserId, PutawayStatusEnum.PUTAWAY, "source_id",
                pageIndex, pageSize);
        return merge(domainPage, PutawayStatusEnum.PUTAWAY);
    }

    /**
     * 查询商家待收卡
     *
     * @param terminalUserId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "duein/search", method = RequestMethod.GET, params = {"terminalUserId", "pageIndex", "pageSize"})
    @ResponseBody
    public Object dueinSearch(Long terminalUserId, Long pageIndex, Long pageSize) {
        DomainPage domainPage = expresspayCardDetailService.allotSearch(terminalUserId, PutawayStatusEnum.INTRANSIT, "organization_id",
                pageIndex, pageSize);
        return merge(domainPage, PutawayStatusEnum.INTRANSIT);
    }

    /**
     * 查询商家已接收卡记录
     *
     * @param terminalUserId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "received/search", method = RequestMethod.GET, params = {"terminalUserId", "pageIndex", "pageSize"})
    @ResponseBody
    public Object receivedSearch(Long terminalUserId, Long pageIndex, Long pageSize) {
        DomainPage domainPage = expresspayCardDetailService.allotSearch(terminalUserId, PutawayStatusEnum.PUTAWAY, "organization_id",
                pageIndex, pageSize);
        return merge(domainPage, PutawayStatusEnum.PUTAWAY);
    }

    /**
     * 确认入库
     *
     * @param cardIdntifier  批次
     * @param terminalUserId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "/putaway", method = RequestMethod.POST, params = {"terminalUserId", "cardIdntifier"})
    @ResponseBody
    public Object validatePutaway(Long terminalUserId, String cardIdntifier) {

        return ImmutableMap.of("result", expresspayCardDetailService.validatePutaway(terminalUserId, cardIdntifier));
    }


    private DomainPage merge(DomainPage domainPage, PutawayStatusEnum statusEnum) {
        List<ExpresspayCardSituationPojo> pojoList = new ArrayList<ExpresspayCardSituationPojo>();
        List<Object[]> list = domainPage.getDomains();
        for (Object[] objects : list) {
            ExpresspayCardSituationPojo situationPojo = new ExpresspayCardSituationPojo();
            situationPojo.setCount(Long.valueOf(objects[0].toString()));
            situationPojo.setUpdateTime(DateUtil.gapDate(objects[1].toString(), "yyyy-MM-dd HH:mm:ss"));
            situationPojo.setOrganizationName(objects[2].toString());
            if (statusEnum.equals(PutawayStatusEnum.INTRANSIT)) {
                situationPojo.setShippingStatus("已从" + objects[2].toString() + "出库，正在发往" + objects[4].toString());
            } else {
                situationPojo.setShippingStatus(objects[4].toString() + "已入库");
            }
            List<Object[]> cards = expresspayCardDetailService.getBycardIdentifier(objects[3].toString());
            situationPojo.setCardIdentifier(objects[3].toString());
            Object[] objList = cards.get(0);
            //判断是否批量入库和扫描入库
            if (objects[5].toString().equals(PutawayStyleEnum.BATCH_QUANTITY.toString())) {
                situationPojo.setUseStatus("1");
                if (CardVarietyEnum.DISK_CARD.toString().equals(objList[1].toString())) {
                    situationPojo.setDiskCard("磁条卡");
                } else {
                    situationPojo.setChipCard("芯片卡");
                }
                situationPojo.setBeginNumber(objList[0].toString());
                Object[] obj = cards.get(cards.size() - 1);
                situationPojo.setEndNumber(obj[0].toString());
            } else {
                situationPojo.setUseStatus("2");
                List<ExpresspayCardPojo> diskCardPojoList = new ArrayList<ExpresspayCardPojo>();
                List<ExpresspayCardPojo> chipCardPojoList = new ArrayList<ExpresspayCardPojo>();
                for (int i = 0; i < cards.size(); i++) {
                    Object[] obj = cards.get(i);
                    ExpresspayCardPojo diskCardPojo = new ExpresspayCardPojo();
                    ExpresspayCardPojo chipCardPojo = new ExpresspayCardPojo();
                    if (CardVarietyEnum.DISK_CARD.toString().equals(obj[1].toString())) {
                        situationPojo.setDiskCard("磁条卡");
                        diskCardPojo.setCardNumber(obj[0].toString());
                        diskCardPojoList.add(diskCardPojo);
                    } else {
                        situationPojo.setChipCard("芯片卡");
                        chipCardPojo.setCardNumber(obj[0].toString());
                        chipCardPojoList.add(chipCardPojo);
                    }
                }
                situationPojo.setChipCardPojoList(chipCardPojoList.size() == 0 ? null : chipCardPojoList);
                situationPojo.setDiskCardPojoList(diskCardPojoList.size() == 0 ? null : diskCardPojoList);
            }
            pojoList.add(situationPojo);
        }
        domainPage.setDomains(pojoList);
        return domainPage;
    }

}