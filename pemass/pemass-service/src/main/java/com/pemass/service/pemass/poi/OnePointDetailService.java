/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.poi.OnePointDetail;
import com.pemass.pojo.poi.OrderPointPayDetailPojo;

import java.util.List;

/**
 * @Description: OnePointDetailService
 * @Author: lin.shi
 * @CreateTime: 2015-06-11 21:21
 */
public interface OnePointDetailService {
    /**
     * 查询被朋友赠送的  或是赠送给朋友的 积分余额
     * @param userId
     * @param isGive 是否是被赠送（true 积分为朋友赠送的，false为自己送给朋友的）
     * @return
     */
    Long selectPointGivingAmount(long userId, boolean isGive);



    /**
     * C端统计一元购积分
     * @param uid 用户id
     * @return
     */
  //  List<Object[]> pointCount(Long uid);

    /**
     * 查询还款商品列表
     * @param uid
     * @param pageIndex
     *@param pageSize @return
     */
    DomainPage getPointDetail(Long uid, Long pageIndex, Long pageSize);

    /**
     * 赠送积分
     * @param uid
     * @param givingUsername 手机号
     * @param amount    积分数
     * @return
     */
    boolean givePoint(Long uid, String givingUsername, Integer amount);


    /**
     * 查询赠送积分记录
     * @param uid 当前用户
     * @param isGive  是否是被赠送（true：被赠送，false：赠送给朋友）
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage pointGiveSearch(Long uid,Boolean isGive, Long pageIndex, Long pageSize);

    /**
     * 积分回收
     * @param pointDetailId
     * @return
     */
    boolean pointRecycle(Long pointDetailId);


    /**
     * 统计被赠送的积分详情
     * @param uid
     * @param pageSize
     * @param pageIndex
     * @return
     */
    DomainPage<OnePointDetail> selectPointGiveDetailAmount(long uid, long pageSize, long pageIndex);

    /**
     * 查询当前账户一元购剩余总数
     * @param uid
     * @return
     */
    Long getByUseableAmount(Long uid);

    /**
     * 查询积分列表
     * @param username
     * @return
     */
    List<Object[]> getByOnePointSearch(String username);

    /**
     * 查询当前用户剩余积分
     * @param username
     * @return
     */
    Long getByRemainPointCount(String username);

    /**
     * 查询当前用户剩余积分
     * @param uid
     * @return
     */
    Long getByRemainPointCount(Long uid);

    /**
     * 查询当前用户赠送出积分余额
     * @param username
     * @return
     */
    Long getByGivePointCount(String username);

    /**
     * 新增一元购积分明细
     * @param onePointDetail
     * @return
     */
    OnePointDetail insert(OnePointDetail onePointDetail);

    /**
     * 积分扣减
     * @param orderPointPayDetailPojo
     * @return
     */
    Boolean pointDeduct(OrderPointPayDetailPojo orderPointPayDetailPojo);


    /**
     * 归还用户积分
     *
     * @param order
     */
    void usePointReturn(Order order);

    /**
     * 还款清分
     * @param uid
     */
    void clearPoint(Long uid);


    /**
     * 用户一键还款
     * @param uid
     * @return
     */
    Boolean repayment(Long uid);


}