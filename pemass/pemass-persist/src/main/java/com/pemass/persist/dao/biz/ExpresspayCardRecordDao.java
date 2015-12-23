/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.biz.ExpresspayCard;
import com.pemass.persist.enumeration.OrderItemStatusEnum;

import java.util.Date;
import java.util.Map;

/**
 * @Description: ExpresspayCardDao
 * @Author: zhou hang
 * @CreateTime: 2015-04-18 16:47
 */
public interface ExpresspayCardRecordDao extends BaseDao {


    /**
     * 查询
     * @param cardNumber 卡号
     * @param organizationId  所属商家id
     * @param statusEnum 使用状态
     * @return
     */
    ExpresspayCard findEntity(String cardNumber, Long organizationId, OrderItemStatusEnum statusEnum);

    /**
     * 根据中银通销售记录
     * @param map
     * @param conMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage findByCondition(Map<String, Object> map, Map<String, Object> conMap, long pageIndex, long pageSize);
}
