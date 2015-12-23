/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.poi.OnePointDetail;

import java.util.List;

/**
 * @Description: OnePointDetailDao
 * @Author: lin.shi
 * @CreateTime: 2015-06-11 21:35
 */
public interface OnePointDetailDao extends BaseDao {

    /**
     * 统计  被朋友赠送，或是 赠送给朋友的  积分 余额
     * @param userId  当前用户
     * @param isGive true 为被朋友赠送的， false  为送给朋友的
     *
     * @return
     */
    Long selectPointGiveAmount(long userId,boolean isGive);

    /**
     * C端统计一元购积分
     * @param uid 用户id
     * @return
     */
    List<Object[]> pointCount(Long uid);

    /**
     * 查询还款商品列表
     * @param uid
     * @param pageIndex
     *@param pageSize @return
     */
    DomainPage getPointDetail(Long uid, long pageIndex, long pageSize);

    /**
     * 根据user查询可用的 一元购积分
     * @param uid
     * @return
     */
    List<OnePointDetail> selectDetailByUserId(Long uid);

    /**
     * 获取用户的一圆购积分详情
     * @param uid
     * @return
     */
    // List<OnePointDetail> selectPointDetail(long uid);

    /**
     * 查询赠送积分记录并统计赚取的还款额
     * @param uid 当前用户
     * @param isGive  是否是被赠送（true：被赠送，false：赠送给朋友）
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage pointGiveSearch(long uid,boolean isGive, long pageIndex, long pageSize);


    /**
     * 根据赠送的 积分详情主键获取当前积分详情信息
     * @param id 详情主键
     * @return
     */
    OnePointDetail selectPointGiveDetail(long id);

    /**
     * 统计被赠送的积分详情列表
     * @param uid 当前的用户
     * @param isGiven 是否是被赠送
     * @param pageSize
     * @param pageIndex
     * @return
     */
    DomainPage selectPointGiveDetailAmount(long uid, long pageSize, long pageIndex);

    /**
     * 查询当前账户一元购剩余总数
     * @param uid
     * @return
     */
    Long getByUseableAmount(Long uid);

    /**
     * 查询积分列表
     * @param uid
     * @return
     */
    List<Object[]> getByOnePointSearch(Long uid);

    /**
     * 查询当前用户剩余积分
     * @param uid
     * @return
     */
    Long getByRemainpointCount(Long uid);

    /**
     * 查询当前用户赠送出积分余额
     * @param uid
     * @return
     */
    Long getByGivePointCount(Long uid);

    /**
     * 根据积分所属用户，积分使用用户获取积分详情
     * @param uid  使用人
     * @param belongUserId   归属人
     * @return
     */
    List<OnePointDetail> selectDetail(Long uid,Long belongUserId);

    /**
     * 还款清分
     * @param uid
     */
    void clearPoint(Long uid);
}