/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.biz.ExpresspayCardDetail;
import com.pemass.persist.enumeration.PutawayStatusEnum;

import java.util.List;
import java.util.Map;

/**
 * @Description: ExpresspayCardDetailDao
 * @Author: zhou hang
 * @CreateTime: 2015-04-23 11:08
 */
public interface ExpresspayCardDetailDao extends BaseDao {

    /**
     * 查询商家调拨中记录
     *
     * @param organizationId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage allotSearch(Long organizationId,PutawayStatusEnum statusEnum,String condition,long pageIndex, long pageSize);

    /**
     * 根据批次 查询卡号
     * @param identifier
     * @return
     */
    List<Object[]> getBycardIdentifier(String identifier);

    /**
     * 查询商家批次数量
     * @param organizationId
     * @return
     */
    Long allotCount(Long organizationId,String  company);

    /**
     * 更新记录状态
     *
     * @param organizationId
     * @param cardIdntifier 批次
     * @return
     */
    boolean updatePutaways(Long organizationId, String cardIdntifier);

    /**
     * 调拨卡记录
     * @param map
     * @param conditiond
     *@param pageIndex
     * @param pageSize   @return
     */
    DomainPage findByPutawayList(Map<String, Object> map, String conditiond, long pageIndex, long pageSize);

}
