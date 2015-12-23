/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.poi.PointCoupon;
import com.pemass.persist.domain.jpa.poi.UserPointDetail;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.vo.PointAreaVO;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.pojo.poi.OrderPointPayDetailPojo;
import com.pemass.pojo.poi.UserPointAggregationPojo;

import java.util.List;

/**
 * @Description: PointDetailService
 * @Author: zhou hang
 * @CreateTime: 2014-11-25 11:09
 */
public interface UserPointDetailService {


    /**
     * 根据用户积分明细ID查询
     *
     * @param userPointDetailId
     * @return
     */
    UserPointDetail getById(Long userPointDetailId);

    /**
     * 新增记录
     *
     * @param userPointDetail
     * @return
     */
    UserPointDetail insert(UserPointDetail userPointDetail);


    /**
     * 获取可用积分中包含某个商户认购的某一批积分的用户信息
     *
     * @param pointPurchaseIds 商户认购的所有积分批次的id集合
     * @param pageIndex        当前页
     * @param pageSize         页大小
     * @return
     */
    DomainPage selectByPointPurchaseIds(List<Long> pointPurchaseIds, long pageIndex, long pageSize);

    /**
     * 查询用户的可用的积分
     *
     * @param uid
     * @param typeEnum
     * @return
     */
    List<UserPointDetail> selectByUserId(Long uid, PointTypeEnum typeEnum);

    /**
     * 根据认购id查询用户积分明细列表
     *
     * @param pointPurchaseId
     * @return
     */
    List<UserPointDetail> selectByPointPurchaseId(Long pointPurchaseId);

    /**
     * 删除积分明细
     *
     * @param pointDetailId
     */
    void deletePointDetail(Long pointDetailId);


    /**
     * 增加积分明细
     *
     * @param coupon
     */
    void insertFromPointCoupon(PointCoupon coupon);

    Boolean deductionPointE4Order(User user, Long orderId, Integer amount, PointAreaVO pointAreaVO);

    Boolean deductionPointP4Order(User user, Long orderId, Integer amount, PointAreaVO pointAreaVO);

    Boolean deductionPointE4CustomizationOrder(User user, Long orderId, Integer amount, PointAreaVO pointAreaVO);

    Boolean deductionPointP4CustomizationOrder(User user, Long orderId, Integer amount, PointAreaVO pointAreaVO);

    Boolean deductionPoint(User user, ConsumeTypeEnum consumeType, Long consumeTargetId, PointTypeEnum pointType, Integer amount, PointAreaVO pointAreaVO);

    /**
     * 抵扣积分
     *
     * @param user            用户ID
     * @param consumeType     消费类型
     * @param consumeTargetId 消费目标ID
     * @param pointType       积分类型
     * @param amount          数量
     * @param provinceId      省份
     * @param cityId          城市
     * @param districtId      地区
     * @param siteId          营业点
     * @param organizationId  机构ID
     */
    Boolean deductionPoint(User user, ConsumeTypeEnum consumeType, Long consumeTargetId, PointTypeEnum pointType, Integer amount,
                           Long provinceId, Long cityId, Long districtId, Long siteId, Long organizationId);


    /**
     * 用户订单消耗积分
     *
     * @param orderPointPayDetailList
     */
    void usePoint(List<OrderPointPayDetailPojo> orderPointPayDetailList);

    /**
     * 归还用户积分
     *
     * @param order
     */
    void usePointReturn(Order order);

    /**
     * 根据用户ID查询用户<strong>通用</strong>E积分明细
     *
     * @param uid       用户ID
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectGeneralPointPByUserId(Long uid, long pageIndex, long pageSize);

    /**
     * 根据用户ID查询用户 E通币 明细
     *
     * @param uid       用户ID
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectGeneralPointEByUserId(Long uid, long pageIndex, long pageSize);


    /**
     * 根据用户ID查询用户<strong>定向</strong>E积分明细
     *
     * @param uid       用户ID
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectDirectionPointPDetailByUserId(Long uid, long pageIndex, long pageSize);

    /**
     * 查询当前用户的定向积分收支明细
     *
     * @param uid         用户
     * @param pointPoolId 定向积分发放方ID
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectUserDirectionPointDetail(Long uid, Long pointPoolId, long pageIndex, long pageSize);

    /**
     * 获取当前用户的E积分可用明细
     *
     * @param uid
     * @param isDirection 是否是定向积分
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectUserPointDetailP(Long uid, boolean isDirection, long pageIndex, long pageSize);


    /**
     * 查询用户过期的积分明细
     *
     * @return
     */
    List selectExpireUserPointDetail();


    /**
     * 统计当前用户的通用E积分总数
     *
     * @param uid
     * @return
     */
    Integer getUserPointDetailGeneralPCount(Long uid);

    /**
     * 根据用户ID，积分类型，是否定向和排序字段查询用户积分明细
     *
     * @param uid              用户ID
     * @param pointType        积分类型
     * @param isGeneral        是否通用
     * @param orderByFiledName 排序字段
     * @param orderBy          排序方式
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage<UserPointDetail> selectByPointTypeAndIsGeneral(Long uid, PointTypeEnum pointType, Integer isGeneral, String orderByFiledName, BaseDao.OrderBy orderBy, long pageIndex, long pageSize);

    Integer getUserPointDetailECount(Long uid);

    /**
     * 根据积分池id获取该批积分的用户存量
     *
     * @param pointPoolId 积分池id
     * @return
     */
    Integer getUserPointAmountByPointPoolId(Long pointPoolId);

    /**
     * 根据积分池id获取该批积分的逾期总量
     *
     * @param pointPoolId 积分池id
     * @return
     */
    Integer getExpiredAmountByPointPoolId(Long pointPoolId);

    /**
     * 统计用户的积分信息
     * <p/>
     * 包括：可用E通币数量、可用E积分数量、可用通用E积分数量、可用定向E积分数量
     *
     * @param userId
     * @param vo
     * @return
     */
    UserPointAggregationPojo getUserPointAggregation(Long userId, PointAreaVO vo);


    void updateUseableAmountById(Long id, Integer useableAmount);

}
