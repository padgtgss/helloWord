/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;

import java.util.List;

/**
 * @Description: PresentSquareDao
 * @Author: zhou hang
 * @CreateTime: 2014-12-10 10:37
 */
public interface PresentSquareDao extends BaseDao {

    /**
     * 根据营销策略类型和用户id,查询出获得商家红包的id
     * @param uid
     * @return
     */
    List<Object[]> selectByUserId(Long uid);
    /**
     * 获取红包广场详情
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectPresentSquare(long pageIndex, long pageSize);

    /**
     * 根据策略ID获取该策略在红包广场下的红包剩余个数
     *
     * @param issueStrategyId
     * @return
     */
    Integer getTotalByIssueStrategyId(Long issueStrategyId);
}
