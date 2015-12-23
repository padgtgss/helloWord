/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;

import java.util.List;

/**
 * @Description: OrderTicketDao
 * @Author: pokl.huang
 * @CreateTime: 2015-06-10 14:18
 */
public interface OrderTicketDao extends BaseDao {
    DomainPage selectTickets(Long uid, List<String> orderItemStatusEnums, long pageIndex, long pageSize);
}