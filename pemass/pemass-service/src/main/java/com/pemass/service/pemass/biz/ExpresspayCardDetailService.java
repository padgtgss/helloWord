/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.persist.domain.jpa.biz.ExpresspayCardDetail;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.PutawayStatusEnum;

import java.util.Date;
import java.util.List;

/**
 * @Description: ExpresspayCardDetailService
 * @Author: zhou hang
 * @CreateTime: 2015-04-23 11:05
 */
public interface ExpresspayCardDetailService {
    /**
     * 查询商家调拨记录
     *
     * @param terminalUserId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage allotSearch(Long terminalUserId,PutawayStatusEnum statusEnum,String condition, long pageIndex, long pageSize);

    /**
     * 根据批次查询卡号
     * @param identifier
     * @return
     */
    List<Object[]> getBycardIdentifier(String identifier);

    /**
     * 查询商家批次数量
     * @param terminalUserId
     * @return
     */
    Long allotCount(Long terminalUserId,String  company);

    /**
     * 确认入库
     *
     * @param terminalUserId
     * @param cardIdntifier 批次
     * @return
     */
    boolean validatePutaway(Long terminalUserId, String cardIdntifier);

    /**
     * 调拨卡记录
     * @param startTime  起始时间
     * @param endTime 结束时间
     * @param province
     * @param organization  商户id
     * @param cardDetail
     * @param conditionId 条件
     * @param domainPage
     * @param conditionp
     * @param conditiond
     */
    DomainPage findByPutawayList(Date startTime, Date endTime, Province province, Organization organization, ExpresspayCardDetail cardDetail, Long conditionId, DomainPage domainPage, String conditionp, String conditiond);

}
