/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.biz;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.biz.ExpresspayCard;
import com.pemass.persist.enumeration.CardVarietyEnum;
import com.pemass.persist.enumeration.LogisticsStatusEnum;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.pojo.biz.ExpresspayCardCountPojo;
import com.pemass.pojo.biz.ExpresspayCardPojo;
import com.pemass.service.exception.BizError;
import com.pemass.service.pemass.biz.ExpresspayCardDetailService;
import com.pemass.service.pemass.biz.ExpresspayCardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: ExpresspayCardController
 * @Author: zhou hang
 * @CreateTime: 2015-04-16 14:26
 */
@Controller
@RequestMapping("/expresspayCard")
public class ExpresspayCardController {

    @Resource
    private ExpresspayCardService expresspayCardService;

    @Resource
    private ExpresspayCardDetailService expresspayCardDetailService;

    /**
     * 扫描录入
     *
     * @param terminalUserId 收银员
     * @param cardNumber     卡号
     * @param cardCategory   类型
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(method = RequestMethod.POST, params = {"terminalUserId", "cardNumber", "cardCategory"})
    @ResponseBody
    public Object add(Long terminalUserId, String[] cardNumber, String[] cardCategory) {
        if (cardNumber.length <= 0) {
            throw new BaseBusinessException(BizError.CARDNUMBER_IS_NULL);
        }
        if (cardCategory.length <= 0) {
            throw new BaseBusinessException(BizError.CARDCATEGORY_IS_NULL);
        }
        if (cardNumber.length != cardCategory.length) {
            throw new BaseBusinessException(BizError.NOT_MATCHING);
        }
        List<String> list = expresspayCardService.add(terminalUserId, cardNumber, cardCategory);
        List<ExpresspayCardPojo> cardPojoList = new ArrayList<ExpresspayCardPojo>();
        for (String str : list) {
            ExpresspayCardPojo cardPojo = new ExpresspayCardPojo();
            cardPojo.setCardNumber(str);
            cardPojoList.add(cardPojo);
        }
        return cardPojoList;
    }


    /**
     * 批量录入
     *
     * @param terminalUserId  收银员
     * @param beginNumber     起始卡号
     * @param endNumber       结束卡号
     * @param cardVarietyEnum 卡号类型
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(method = RequestMethod.POST, params = {"terminalUserId", "beginNumber", "endNumber", "cardVarietyEnum"})
    @ResponseBody
    public Object adds(Long terminalUserId, String beginNumber,
                       String endNumber, CardVarietyEnum cardVarietyEnum) {

        List<String> list = expresspayCardService.adds(terminalUserId, beginNumber, endNumber, cardVarietyEnum);
        List<ExpresspayCardPojo> cardPojoList = new ArrayList<ExpresspayCardPojo>();
        for (String str : list) {
            ExpresspayCardPojo cardPojo = new ExpresspayCardPojo();
            cardPojo.setCardNumber(str);
            cardPojoList.add(cardPojo);
        }
        return cardPojoList;
    }

    /**
     * 根据卡号 查询详情
     *
     * @param cardNumber     卡号
     * @param terminalUserId 收银员
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "/{cardNumber}", method = RequestMethod.GET,params={"terminalUserId"})
    @ResponseBody
    public Object getById(@PathVariable("cardNumber") String cardNumber, Long terminalUserId) {
        ExpresspayCard expresspayCard = expresspayCardService.getById(cardNumber, terminalUserId);
        ExpresspayCardPojo expresspayCardPojo = new ExpresspayCardPojo();
        expresspayCardPojo.setCardNumber(expresspayCard.getCardNumber());
        expresspayCardPojo.setCostPrice(expresspayCard.getCostPrice());
        expresspayCardPojo.setCardCategory(expresspayCard.getCardCategory().getDescription());
        return expresspayCardPojo;
    }

    /**
     * 扫描调拨
     *
     * @param cardNumber     卡号
     * @param organizationId 调拨账号
     * @param terminalUserId 收银员账号
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "/allot", method = RequestMethod.POST, params = {"cardNumber", "organizationId", "terminalUserId"})
    @ResponseBody
    public Object allot(String[] cardNumber, Long organizationId, Long terminalUserId) {
        if (cardNumber.length <= 0) {
            throw new BaseBusinessException(BizError.CARDNUMBER_IS_NULL);
        }

        return ImmutableMap.of("result", expresspayCardService.updateAllot(cardNumber, organizationId, terminalUserId));
    }

    /**
     * 批量调拨
     *
     * @param beginNumber    起始卡号
     * @param endNumber      结束卡号
     * @param organizationId 调拨商家
     * @param terminalUserId 收银员
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "/allot", method = RequestMethod.POST, params = {"beginNumber", "endNumber", "organizationId", "terminalUserId"})
    @ResponseBody
    public Object allots(String beginNumber, String endNumber, Long organizationId, Long terminalUserId) {
        return ImmutableMap.of("result", expresspayCardService.updateAllots(beginNumber, endNumber, organizationId, terminalUserId));
    }

    /**
     * 库存管理 统计详情
     *
     * @param terminalUserId 收银员id
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "/count/terminalUser/{terminalUserId}", method = RequestMethod.GET)
    @ResponseBody
    public Object count(@PathVariable("terminalUserId") Long terminalUserId) {
        long onSaleCount = expresspayCardService.onSaleCount(terminalUserId, OrderItemStatusEnum.UNUSED, LogisticsStatusEnum.PUTAWAY);
        long soldCount = expresspayCardService.onSaleCount(terminalUserId, OrderItemStatusEnum.USED, LogisticsStatusEnum.PUTAWAY);
        long inTransitCount = expresspayCardDetailService.allotCount(terminalUserId, "organization_id");
        long allotCount = expresspayCardDetailService.allotCount(terminalUserId, "source_id");
        ExpresspayCardCountPojo expresspayCardCountPojo = new ExpresspayCardCountPojo();
        expresspayCardCountPojo.setOnSaleCount(onSaleCount);
        expresspayCardCountPojo.setSoldCount(soldCount);
        expresspayCardCountPojo.setInTransitCount(inTransitCount);
        expresspayCardCountPojo.setAllotCount(allotCount);
        return expresspayCardCountPojo;
    }

    /**
     * 库存管理 查询待售卡详情
     *
     * @param terminalUserId 收银员
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "onSaleCount/search", method = RequestMethod.GET,params = {"terminalUserId","pageIndex","pageSize"})
    @ResponseBody
    public Object onsaleSearch(Long terminalUserId, Long pageIndex, Long pageSize) {
        DomainPage domainPage = expresspayCardService.onsaleSearch(terminalUserId, pageIndex, pageSize);
        List<ExpresspayCard> cardList = domainPage.getDomains();
        List<ExpresspayCardPojo> list = new ArrayList<ExpresspayCardPojo>();
        for (ExpresspayCard expresspayCard : cardList) {
            ExpresspayCardPojo expresspayCardPojo = new ExpresspayCardPojo();
            expresspayCardPojo.setCardNumber(expresspayCard.getCardNumber());
            expresspayCardPojo.setCardCategory(expresspayCard.getCardCategory().getDescription());
            list.add(expresspayCardPojo);
        }
        domainPage.setDomains(list);
        return domainPage;
    }

    /**
     * 库存管理 查询已售卡详情
     *
     * @param terminalUserId 收银员
     * @param cardNumber     卡号
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "soldCount/search", method = RequestMethod.GET,params = {"terminalUserId","pageIndex","pageSize"})
    @ResponseBody
    public Object soldSearch(@RequestParam(value = "cardNumber", required = false) String cardNumber,
                             Long terminalUserId, Long pageIndex, Long pageSize) {
        DomainPage domainPage = expresspayCardService.soldSearch(terminalUserId, cardNumber, pageIndex, pageSize);
        List<Object []> cardList = domainPage.getDomains();
        List<ExpresspayCardPojo> list = new ArrayList<ExpresspayCardPojo>();
        for (Object[] objects : cardList) {
            ExpresspayCardPojo expresspayCardPojo = new ExpresspayCardPojo();
            ExpresspayCard expresspayCard=expresspayCardService.getById(Long.valueOf(objects[0].toString()));
            expresspayCardPojo.setCardNumber(expresspayCard.getCardNumber());
            expresspayCardPojo.setCardCategory(expresspayCard.getCardCategory().getDescription());
            expresspayCardPojo.setTotalPrice(Float.valueOf(objects[1].toString()));
            expresspayCardPojo.setUserName(objects[2].toString());
            expresspayCardPojo.setTelephone(objects[3].toString());
            expresspayCardPojo.setInvoice(objects[4]== null ? null : "1");
            list.add(expresspayCardPojo);
        }
        domainPage.setDomains(list);
        return domainPage;
    }
}