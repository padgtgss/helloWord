/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;

import java.util.Date;
import java.util.List;

/**
 * @Description: IssueStrategyDao
 * @Author: zhou hang
 * @CreateTime: 2015-05-06 14:51
 */
public interface IssueStrategyDao extends BaseDao {

    DomainPage findById(long pageIndex, long pageSize);

    List selectIssueStrategyByField();

    /**
     * 保存红包广场和策略的关系到Redis中
     * <p>
     * 设置过期时间为 expiryTime
     * </p>
     *
     * @param presentSquareId 红包广场ID
     * @param issueStrategyId 策略ID
     * @param expiryTime      过期时间
     * @return
     */
    Boolean bindPresentSquareToIssueStrategy(Long presentSquareId, Long issueStrategyId, Date expiryTime);


    /**
     * 从表头取出一个红包广场ID
     * <p>
     * 如果已经全部取出返回 null
     * </p>
     *
     * @param issueStrategyId
     * @return
     */
    Long popupPresentSquareId(Long issueStrategyId);


    /**
     * 判断用户是否已经领取了该策略下的红包
     * <p>
     * 设置过期时间为 expiryTime
     * </p>
     *
     * @param userId          用户ID
     * @param issueStrategyId 策略ID
     * @param expiryTime      过期时间
     * @return
     */
    Boolean hasAlreadyGrab(Long userId, Long issueStrategyId, Date expiryTime);

}
