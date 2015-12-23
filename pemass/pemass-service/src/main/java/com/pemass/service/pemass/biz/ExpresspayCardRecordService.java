/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.persist.domain.jpa.biz.ExpresspayCard;
import com.pemass.persist.domain.jpa.biz.ExpresspayCardRecord;
import com.pemass.persist.domain.jpa.ec.Invoice;
import com.pemass.persist.domain.jpa.sys.Organization;

import java.util.Date;
import java.util.List;

/**
 * @Description: ExpresspayCardRecordService
 * @Author: zhou hang
 * @CreateTime: 2015-04-17 11:39
 */
public interface ExpresspayCardRecordService {
    /**
     * 销售卡
     * @param expresspayCardRecord 记录卡对象
     * @param cardNumber  卡号
     * @param invoice 发票
     * @param terminalUserId  收银员id
     * @return
     */
    Boolean add(ExpresspayCardRecord expresspayCardRecord,Invoice invoice,String cardNumber, Long terminalUserId);

    /**
     * 审核后台   查询销售中银通卡积分
     * @param expresspayCard  中银通记录卡对象
     * @param invoice 发票
     * @param startDate 起始时间
     * @param endDate 结束时间
     * @param provinceId 省id
     * @param organizationId  商家id
     * @param domainPage
     * @return
     */
    DomainPage findByCondition(ExpresspayCard expresspayCard, Integer invoice, Date startDate, Date endDate,Float startMoney,Float endMoney, Province provinceId, Organization organizationId, DomainPage domainPage,String ids);

}
