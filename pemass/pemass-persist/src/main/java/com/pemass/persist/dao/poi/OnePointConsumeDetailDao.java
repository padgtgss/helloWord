/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.poi.OnePointConsumeDetail;

import java.util.List;

/**
 * @Description: OnePointConsumeDetailDao
 * @Author: lin.shi
 * @CreateTime: 2015-06-11 21:51
 */
public interface OnePointConsumeDetailDao extends BaseDao {

    /**
     * 根据用户与订单编号 统计 当前订单消耗积分(分别统计消耗朋友赠送的积分与自己的积分)
     * @param uid
     * @param orderId
     * @return
     */
    List<Object[]> consumeDetailCountByOrder(Long uid,Long orderId);


    /**
     * 根据用户Id  统计已赚还款数
     * @param uid
     * @return
     */
    Double getConsumeDetailCout(Long uid);

    /**
     * 统计当前用户的已赚还款数（分别统计自己赚的跟朋友赚的）
     * @param uid
     * @return
     */
    Object[] getProfitCount(Long uid);

    /**
     * 获取当前用户跟朋友的还款额赚取记录
     * @param uid  当前用户
     * @param isYouself  是否是当前用户自己赚取的
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectProfitDetails(long uid,boolean isYouself,long pageIndex,long pageSize);

    /**
     * 获取当前用户为某一个朋友花费的壹购积分记录
     * @param belongUserName
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectForFriendDetail( Long uid, String belongUserName, long pageIndex, long pageSize);


    /**
     * 获取朋友赠送的壹购积分动结数
     * @param uid
     * @return
     */
    Long getFreeGivingPointAmount(Long uid);
}