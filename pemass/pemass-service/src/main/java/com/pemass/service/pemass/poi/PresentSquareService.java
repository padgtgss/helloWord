/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.poi.Present;
import com.pemass.persist.domain.jpa.poi.PresentSquare;

import java.util.List;
import java.util.Map;

/**
 * @Description: PresentSquareService
 * @Author: zhou hang
 * @CreateTime: 2014-12-10 10:19
 */
public interface PresentSquareService {


    /**
     * 根据营销策略类型，查询商家发放到 红包广场的红包
     *
     * @param issueStrategyId
     * @return
     */
    List<PresentSquare> selectByIssueId(Long issueStrategyId);

    /**
     * 根据营销策略类型和用户id,查询出获得商家红包的id
     *
     * @param uid
     * @return
     */
    List<Object[]> selectByUserId(Long uid);

    /**
     * 用户抢红包
     * <p/>
     * 返回已抢到的红包
     *
     * @param userId
     * @param issueStrategyId
     * @return
     */
    Present grabPresent(Long userId, Long issueStrategyId);

    Present grabAndUnpack(Long userId, Long issueStrategyId);

    /**
     * 获取红包广场的红包详情
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectAllPresentSquare(long pageIndex, long pageSize);

    /**
     * 查询红包广场集合
     *
     * @param map
     * @return
     */
    List<PresentSquare> selectPresentSquareList(Map<String, Object> map);

    /**
     * 删除红包广场
     *
     * @param presentSquareId
     */
    void deletePresentSquare(Long presentSquareId);


    /**
     * 根据uid和策略ID 判断该用户是否已领取过 该策略发放到红包广场上的红包
     *
     * @param uid
     * @param issueStrategyId
     * @return
     */
    boolean hasGrabPresent(Long uid, Long issueStrategyId);

    /**
     * 根据红包广场id查询
     *
     * @param presentSquareId
     * @return
     */
    PresentSquare getPresentSquareById(Long presentSquareId);

    /**
     * 修改红包-广场
     *
     * @param presentSquare 红包-广场实体
     * @return
     */
    PresentSquare updatePresentSquare(PresentSquare presentSquare);

    /**
     * 根据策略ID获取该策略在红包广场下的红包剩余个数
     *
     * @param issueStrategyId
     * @return
     */
    Integer getTotalByIssueStrategyId(Long issueStrategyId);
}
