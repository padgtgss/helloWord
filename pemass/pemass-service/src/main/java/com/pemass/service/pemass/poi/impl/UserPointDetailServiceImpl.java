/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.dao.poi.UserPointDetailDao;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.persist.domain.jpa.poi.PointCoupon;
import com.pemass.persist.domain.jpa.poi.PointPoolOrganization;
import com.pemass.persist.domain.jpa.poi.PointPurchase;
import com.pemass.persist.domain.jpa.poi.UserConsumeDetail;
import com.pemass.persist.domain.jpa.poi.UserPointDetail;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.vo.PointAreaVO;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PointChannelEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.pojo.poi.OrderPointPayDetailPojo;
import com.pemass.pojo.poi.UserPointAggregationPojo;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.exception.PoiError;
import com.pemass.service.pemass.biz.ProductService;
import com.pemass.service.pemass.biz.ProductSnapshotService;
import com.pemass.service.pemass.ec.OrderItemService;
import com.pemass.service.pemass.poi.OnePointDetailService;
import com.pemass.service.pemass.poi.PointPoolOrganizationService;
import com.pemass.service.pemass.poi.UserPointDetailService;
import com.pemass.service.pemass.sys.UserService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @Description: PointDetailServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2014-11-25 14:08
 */
@Service
public class UserPointDetailServiceImpl implements UserPointDetailService {

    @Resource
    private UserPointDetailDao userPointDetailDao;

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private ProductService productService;

    @Resource
    private OrderItemService orderItemService;

    @Resource
    private ProductSnapshotService productSnapshotService;

    @Resource
    private UserService userService;

    @Resource
    private OnePointDetailService onePointDetailService;

    @Resource
    private PointPoolOrganizationService pointPoolOrganizationService;

    @Override
    public UserPointDetail getById(Long userPointDetailId) {
        Preconditions.checkNotNull(userPointDetailId);
        return jpaBaseDao.getEntityById(UserPointDetail.class, userPointDetailId);
    }

    @Override
    public UserPointDetail insert(UserPointDetail userPointDetail) {
        Preconditions.checkNotNull(userPointDetail);
        jpaBaseDao.persist(userPointDetail);
        return userPointDetail;
    }

    @Override
    public DomainPage selectByPointPurchaseIds(List<Long> pointPurchaseIds, long pageIndex, long pageSize) {
        DomainPage domainPage = userPointDetailDao.selectUsersByPointPurchaseId(pointPurchaseIds, pageIndex, pageSize);
        DomainPage newDomainPage = this.PosttingDomainPage(domainPage);
        return newDomainPage;
    }

    /**
     * 重新封装
     *
     * @param domainPage
     * @return
     */
    private DomainPage PosttingDomainPage(DomainPage domainPage) {
        List result = new ArrayList();
        String area;
        String urlStr;
        for (Object object : domainPage.getDomains()) {
            Object[] obj = new Object[2];
            UserPointDetail pointDetail = (UserPointDetail) object;
            User user = jpaBaseDao.getEntityById(User.class, pointDetail.getUserId());
            obj[0] = user;
            urlStr = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=P3T7zML1eu48NaVhb6ZweZlG&location="
                    + user.getLatitude() + "," + user.getLongitude();
            area = getUserArea(urlStr);
            obj[1] = area;
            result.add(obj);
        }
        DomainPage newDomainPage = domainPage;
        newDomainPage.getDomains().clear();
        newDomainPage.getDomains().addAll(result);
        return newDomainPage;
    }

    /**
     * 根据url地址获取json格式的字符串，并转换成Map
     *
     * @param urlStr
     * @return
     */
    private String getUserArea(String urlStr) {
        StringBuffer json = new StringBuffer();
        String location = urlStr.substring(urlStr.lastIndexOf("=") + 1);
        String area = null;
        if ("null,null".equals(location)) {
            area = "无区域";
        } else {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStreamReader input = new InputStreamReader(conn.getInputStream(), "utf-8");
                Scanner inputStream = new Scanner(input);
                while (inputStream.hasNext()) {
                    json.append(inputStream.nextLine());
                }
                Map<String, Object> results;
                Map<String, Object> maps;
                maps = new ObjectMapper().readValue(json.toString(), Map.class);
                Integer status = (Integer) maps.get("status");
                if (status == 0) {
                    results = (Map<String, Object>) maps.get("result");
                    area = (String) results.get("formatted_address");
                } else {
                    area = "无区域";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return area;
    }

    @Override
    public List<UserPointDetail> selectByUserId(Long uid, PointTypeEnum typeEnum) {
        return userPointDetailDao.selectByUserId(uid, typeEnum);
    }

    /**
     * 根据认购id查询用户积分明细列表
     *
     * @param pointPurchaseId
     * @return
     */
    @Override
    public List<UserPointDetail> selectByPointPurchaseId(Long pointPurchaseId) {
        return jpaBaseDao.getEntitiesByField(UserPointDetail.class, "pointPurchase.id", pointPurchaseId);
    }

    @Override
    public void deletePointDetail(Long pointDetailId) {
        UserPointDetail pointDetail = jpaBaseDao.getEntityById(UserPointDetail.class, pointDetailId);
        pointDetail.setAvailable(AvailableEnum.UNAVAILABLE);
        jpaBaseDao.merge(pointDetail);
    }

    @Override
    public void insertFromPointCoupon(PointCoupon coupon) {
        UserPointDetail pointDetail = new UserPointDetail();
        PointPurchase pointPurchase = jpaBaseDao.getEntityById(PointPurchase.class, coupon.getPointPurchaseId());

        pointDetail.setAmount(coupon.getAmount());
        pointDetail.setArea(PemassConst.AREA_GENERAL);
        pointDetail.setExpiryTime(pointPurchase.getExpiryTime());
        pointDetail.setOrganizationId(coupon.getOrganizationId());
        pointDetail.setPointPurchaseId(coupon.getPointPurchaseId());
        pointDetail.setPointType(coupon.getPointType());
        pointDetail.setPointChannel(PointChannelEnum.POINT_COUPON);
        pointDetail.setUseableAmount(coupon.getAmount());
        pointDetail.setUserId(coupon.getUserId());
        jpaBaseDao.persist(pointDetail);
    }


    @Override
    public Boolean deductionPointE4Order(User user, Long orderId, Integer amount, PointAreaVO pointAreaVO) {
        return this.deductionPoint(user, ConsumeTypeEnum.ORDER, orderId, PointTypeEnum.E, amount,
                pointAreaVO.getProvinceId(), pointAreaVO.getCityId(), pointAreaVO.getDistrictId(), pointAreaVO.getSiteId(), pointAreaVO.getOrganizationId());
    }

    @Override
    public Boolean deductionPointP4Order(User user, Long orderId, Integer amount, PointAreaVO pointAreaVO) {
        return this.deductionPoint(user, ConsumeTypeEnum.ORDER, orderId, PointTypeEnum.P, amount,
                pointAreaVO.getProvinceId(), pointAreaVO.getCityId(), pointAreaVO.getDistrictId(), pointAreaVO.getSiteId(), pointAreaVO.getOrganizationId());
    }

    @Override
    public Boolean deductionPointE4CustomizationOrder(User user, Long orderId, Integer amount, PointAreaVO pointAreaVO) {
        return this.deductionPoint(user, ConsumeTypeEnum.CUSTOMIZATION, orderId, PointTypeEnum.E, amount,
                pointAreaVO.getProvinceId(), pointAreaVO.getCityId(), pointAreaVO.getDistrictId(), pointAreaVO.getSiteId(), pointAreaVO.getOrganizationId());
    }

    @Override
    public Boolean deductionPointP4CustomizationOrder(User user, Long orderId, Integer amount, PointAreaVO pointAreaVO) {
        return this.deductionPoint(user, ConsumeTypeEnum.CUSTOMIZATION, orderId, PointTypeEnum.P, amount,
                pointAreaVO.getProvinceId(), pointAreaVO.getCityId(), pointAreaVO.getDistrictId(), pointAreaVO.getSiteId(), pointAreaVO.getOrganizationId());
    }

    @Override
    public Boolean deductionPoint(User user, ConsumeTypeEnum consumeType, Long consumeTargetId, PointTypeEnum pointType, Integer amount, PointAreaVO pointAreaVO) {
        if (pointAreaVO == null) {
            return deductionPoint(user, consumeType, consumeTargetId, pointType, amount, null, null, null, null, null);
        }
        return deductionPoint(user, consumeType, consumeTargetId, pointType, amount, pointAreaVO.getProvinceId(), pointAreaVO.getCityId(), pointAreaVO.getDistrictId(), pointAreaVO.getSiteId(), pointAreaVO.getOrganizationId());
    }

    @Override
    public Boolean deductionPoint(User user, ConsumeTypeEnum consumeType, Long consumeTargetId, PointTypeEnum pointType, Integer amount,
                                  Long provinceId, Long cityId, Long districtId, Long siteId, Long organizationId) {
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(consumeTargetId);
        Preconditions.checkNotNull(amount);
        if (amount == 0) {
            return true;
        }
        //获取用户可用积分
        Expression userIdExpression = new Expression("userId", Operation.Equal, user.getId());
        Expression pointTypeExpression = new Expression("pointType", Operation.Equal, pointType);
        Expression expiryTimeExpression = new Expression("expiryTime", Operation.GreaterThanEqual, new Date());
        Expression useableAmountExpression = new Expression("useableAmount", Operation.GreaterThan, new Integer(0));
        List<Expression> expressions = Lists.newArrayList(userIdExpression, pointTypeExpression, expiryTimeExpression, useableAmountExpression);
        DomainPage<UserPointDetail> domainPage = jpaBaseDao.getEntitiesPagesByExpressionList(UserPointDetail.class, expressions, "area", BaseDao.OrderBy.ASC, 0, Integer.MAX_VALUE);

        List<UserPointDetail> pointDetailList = domainPage.getDomains();
        if ((pointDetailList == null) || (pointDetailList.size() == 0)) {
            if (pointType == PointTypeEnum.E) {
                throw new BaseBusinessException(PoiError.POINT_E_NOT_ENOUGH);
            } else {
                throw new BaseBusinessException(PoiError.POINT_P_NOT_ENOUGH);
            }
        }

        /*-- 循环迭代UserPointDetailList --*/
        for (UserPointDetail userPointDetail : pointDetailList) {
            if (!this.validateArea(userPointDetail.getArea(), provinceId, cityId, districtId, siteId, organizationId)) {
                continue;
            }

            UserConsumeDetail userConsumeDetail = new UserConsumeDetail();
            userConsumeDetail.setUserPointDetailId(userPointDetail.getId());
            userConsumeDetail.setConsumeType(consumeType);
            userConsumeDetail.setConsumeTargetId(consumeTargetId);
            userConsumeDetail.setPointType(pointType);
            userConsumeDetail.setAmount(0);
            userConsumeDetail.setUserPointDetailId(userPointDetail.getId());

            int useableAmount = userPointDetail.getUseableAmount();
            if (userPointDetail.getUseableAmount() < amount) {
//                userPointDetail.setUseableAmount(0);
                userPointDetailDao.updateUseableAmountById(userPointDetail.getId(), userPointDetail.getUseableAmount());
//                jpaBaseDao.merge(userPointDetail);

                userConsumeDetail.setPayableAmount(useableAmount);
                jpaBaseDao.persist(userConsumeDetail);
            } else {
//                userPointDetail.setUseableAmount(useableAmount - amount);
//                jpaBaseDao.merge(userPointDetail);
                userPointDetailDao.updateUseableAmountById(userPointDetail.getId(), amount);

                userConsumeDetail.setPayableAmount(amount);
                jpaBaseDao.persist(userConsumeDetail);
                amount = 0;
                break;  //当进入else表示循环结束
            }
            amount = amount - useableAmount;
        } //end of for

        //当积分不足时，抵扣失败
        if (amount > 0) {
            if (pointType == PointTypeEnum.E) {
                throw new BaseBusinessException(PoiError.POINT_E_NOT_ENOUGH);
            } else {
                throw new BaseBusinessException(PoiError.POINT_P_NOT_ENOUGH);
            }
        }

        return true;
    }

    private Boolean validateArea(String pointArea, PointAreaVO pointAreaVO) {
        return this.validateArea(pointArea, pointAreaVO.getProvinceId(), pointAreaVO.getCityId(), pointAreaVO.getDistrictId(), pointAreaVO.getSiteId(), pointAreaVO.getOrganizationId());
    }


    /**
     * 判断积分定向是否可以使用
     * <p/>
     * 积分区域的规则为：省id:市id:区id:营业点id:商家id
     * 当商家ID为"*"时，表示特约商户
     * 如：
     * <p/>
     * 全国通用：00:00:00:00:00
     * 定向到省：1,2:00:00:00:00
     * 定向到市：00:1,2:00:00:00
     * 定向到区：00:00:1,2:00:00
     * 定向到营业点：00:00:00:1,2:00
     * 定向到商家：00:00:00:00:*
     * <p/>
     * 其中省，市，区没有承继关系。
     *
     * @param pointArea      区域字符串
     * @param provinceId     省份ID
     * @param cityId         城市ID
     * @param districtId     地区ID
     * @param siteId         营业点ID
     * @param organizationId 机构ID
     * @return true表示可以校验成功(即可以使用该条积分明细记录)
     */
    private Boolean validateArea(String pointArea, Long provinceId, Long cityId, Long districtId, Long siteId, Long organizationId) {
        String empty = "00";
        Preconditions.checkNotNull(pointArea);
        List<String> areas = Splitter.on(PemassConst.AREA_SEPARATOR_SYMBOL).splitToList(pointArea);
        if (areas.size() != 5) {
            throw new IllegalArgumentException("Point Area Format Error");
        }

        /*-- 分别校验各字段 --*/
        //判断是否通用
        if (PemassConst.AREA_GENERAL.equals(pointArea)) {
            return true;
        }

        //判断商家ID
        if (!empty.equals(areas.get(4)) && organizationId != null) {
            List<PointPoolOrganization> pointPoolOrganizationList = pointPoolOrganizationService.selectByOrganizationId(organizationId);
            if (pointPoolOrganizationList.size() > 0) {
                return true;
            }
        }

        //判断营业点
        if (!empty.equals(areas.get(3)) && siteId != null) {
            if (this.isContains(areas.get(3), siteId)) {
                return true;
            }
        }

        //判断地区
        if (!empty.equals(areas.get(2)) && districtId != null) {
            if (this.isContains(areas.get(2), districtId)) {
                return true;
            }
        }

        //判断市
        if (!empty.equals(areas.get(1)) && cityId != null) {
            if (this.isContains(areas.get(1), cityId)) {
                return true;
            }
        }
        //判断省份
        if (!empty.equals(areas.get(0)) && provinceId != null) {
            if (this.isContains(areas.get(0), provinceId)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 指定区域内是否包含目标ID
     *
     * @param subarea  区域(ID直接之间使用","进行分割)
     * @param targetId 目标ID
     * @return true表示包含
     */
    private Boolean isContains(String subarea, Long targetId) {
        List<String> siteIdList = Splitter.on(SystemConst.SEPARATOR_SYMBOL).splitToList(subarea);
        for (String siteIdStr : siteIdList) {
            if (siteIdStr.equals(targetId + "")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void usePoint(List<OrderPointPayDetailPojo> orderPointPayDetailList) {
        for (OrderPointPayDetailPojo orderPointPayDetail : orderPointPayDetailList) {
            Order order = jpaBaseDao.getEntityById(Order.class, orderPointPayDetail.getOrderId());
            List<OrderItem> orderItemList = orderItemService.selectByOrderId(order.getId());
            ProductSnapshot productSnapshot = productSnapshotService.getSnapshotById(orderItemList.get(0).getProductSnapshotId());
            Product product = productService.getProductInfo(productSnapshot.getProductId());
            User belongUser = userService.getById(orderPointPayDetail.getBelongUserId());//所属者
            User user = userService.getById(orderPointPayDetail.getUserId());//使用者
            Integer amount = orderPointPayDetail.getAmount();

            PointAreaVO pointAreaVO = new PointAreaVO(product.getOrganizationId());
            if (PointTypeEnum.P.equals(orderPointPayDetail.getPointType())) {
                this.deductionPointP4Order(user, order.getId(), amount, pointAreaVO);
            } else if (PointTypeEnum.E.equals(orderPointPayDetail.getPointType())) {
                this.deductionPointE4Order(user, order.getId(), amount, pointAreaVO);
            } else {
                OrderPointPayDetailPojo pojo = new OrderPointPayDetailPojo();
                pojo.setBelongUserId(belongUser.getId());
                pojo.setAmount(amount);
                pojo.setUserId(order.getUserId());
                pojo.setOrderId(order.getId());
                onePointDetailService.pointDeduct(pojo);
            }
        }
    }


    /**
     * 归还积分
     *
     * @param order
     */
    @Transactional
    public void usePointReturn(Order order) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("consumeType", ConsumeTypeEnum.ORDER);
        param.put("consumeTargetId", order.getId());

        List<UserConsumeDetail> userConsumeDetailList = jpaBaseDao.getEntitiesByFieldList(UserConsumeDetail.class, param);

        for (UserConsumeDetail userConsumeDetail : userConsumeDetailList) {
            userPointDetailDao.usePointReturn(userConsumeDetail.getUserPointDetailId(), userConsumeDetail.getPayableAmount());
            /**将此条明细逻辑删除**/
            userConsumeDetail.setAvailable(AvailableEnum.UNAVAILABLE);
            jpaBaseDao.merge(userConsumeDetail);
        }

    }

    @Override
    public DomainPage selectGeneralPointPByUserId(Long uid, long pageIndex, long pageSize) {
        return this.selectGeneralPointByUserId(uid, PointTypeEnum.P, pageIndex, pageSize);
    }

    @Override
    public DomainPage selectGeneralPointEByUserId(Long uid, long pageIndex, long pageSize) {
        return this.selectGeneralPointByUserId(uid, PointTypeEnum.E, pageIndex, pageSize);
    }


    private DomainPage selectGeneralPointByUserId(Long uid, PointTypeEnum pointTypeEnum, long pageIndex, long pageSize) {

        return userPointDetailDao.selectUserPointDetail(uid, pointTypeEnum, pageIndex, pageSize);
    }

    @Override
    public List selectExpireUserPointDetail() {
        return userPointDetailDao.selectExpireUserPointDetail();
    }


    @Override
    public Integer getUserPointDetailGeneralPCount(Long uid) {

        return userPointDetailDao.getUserPointDetailGeneralPCount(uid);
    }

    @Override
    public DomainPage<UserPointDetail> selectByPointTypeAndIsGeneral(Long uid, PointTypeEnum pointType, Integer isGeneral, String orderByFiledName, BaseDao.OrderBy orderBy, long pageIndex, long pageSize) {
        Expression uidExpression = new Expression("userId", Operation.Equal, uid);
        Expression pointTypeExpression = new Expression("pointType", Operation.Equal, pointType);
        Expression useableAmountExpression = new Expression("useableAmount", Operation.GreaterThan, Integer.valueOf(0));
        Expression expiryTimeExpression = new Expression("expiryTime", Operation.GreaterThan, new Date());
        Expression isDirectionExpression = null;
        if (isGeneral == 1) {
            isDirectionExpression = new Expression("area", Operation.Equal, PemassConst.AREA_GENERAL);
        } else {
            isDirectionExpression = new Expression("area", Operation.NotEqual, PemassConst.AREA_GENERAL);
        }

        ArrayList<Expression> expressions = Lists.newArrayList(uidExpression, pointTypeExpression, isDirectionExpression, useableAmountExpression, expiryTimeExpression);

        return jpaBaseDao.getEntitiesPagesByExpressionList(UserPointDetail.class, expressions, orderByFiledName, orderBy, pageIndex, pageSize);
    }

    @Override
    public Integer getUserPointDetailECount(Long uid) {

        return userPointDetailDao.getUserPointDetailECount(uid);
    }

    @Override
    public Integer getUserPointAmountByPointPoolId(Long pointPoolId) {
        Preconditions.checkNotNull(pointPoolId);
        return userPointDetailDao.getPointAmountByPointPoolId(pointPoolId);
    }

    @Override
    public Integer getExpiredAmountByPointPoolId(Long pointPoolId) {
        Preconditions.checkNotNull(pointPoolId);
        return userPointDetailDao.getExpiredAmountByPointPoolId(pointPoolId);
    }

    @Override
    public UserPointAggregationPojo getUserPointAggregation(Long userId, PointAreaVO vo) {
        Expression userIdExpression = new Expression("userId", Operation.Equal, userId);
        Expression expiryTimeExpression = new Expression("expiryTime", Operation.GreaterThanEqual, new Date());
        Expression useableAmountExpression = new Expression("useableAmount", Operation.GreaterThan, new Integer(0));
        List<Expression> expressions = Lists.newArrayList(userIdExpression, expiryTimeExpression, useableAmountExpression);
        DomainPage<UserPointDetail> domainPage = jpaBaseDao.getEntitiesPagesByExpressionList(UserPointDetail.class, expressions, "area", BaseDao.OrderBy.ASC, 0, Integer.MAX_VALUE);
        List<UserPointDetail> userPointDetailList = domainPage.getDomains();

        UserPointAggregationPojo pojo = new UserPointAggregationPojo();
        pojo.setUserId(userId);
        pojo.setUseableAmountE(Integer.valueOf(0));
        pojo.setUseableAmountP(Integer.valueOf(0));
        pojo.setGeneralAmountP(Integer.valueOf(0));
        pojo.setDirectionAmountP(Integer.valueOf(0));
        pojo.setUseableAmountE(Integer.valueOf(0));
        for (UserPointDetail detail : userPointDetailList) {

            if (vo != null && !this.validateArea(detail.getArea(), vo)) {
                continue;
            }

            switch (detail.getPointType()) {
                case P:
                    pojo.setUseableAmountP(pojo.getUseableAmountP() + detail.getUseableAmount());
                    if ("00:00:00:00:00".equals(detail.getArea())) {
                        pojo.setGeneralAmountP(pojo.getGeneralAmountP() + detail.getUseableAmount());
                    } else {
                        pojo.setDirectionAmountP(pojo.getDirectionAmountP() + detail.getUseableAmount());
                    }
                    break;
                case E:
                    pojo.setUseableAmountE(pojo.getUseableAmountE() + detail.getUseableAmount());
                    break;
                case O:
                    //一元购为另外一张表
                    break;
            }
        }
        return pojo;
    }

    @Override
    public void updateUseableAmountById(Long id, Integer useableAmount) {
        userPointDetailDao.updateUseableAmountById(id,useableAmount);
    }


    @Override
    public DomainPage selectDirectionPointPDetailByUserId(Long uid, long pageIndex, long pageSize) {

        return userPointDetailDao.selectDirectionPointDetailByPool(uid, pageIndex, pageSize);
    }

    @Override
    public DomainPage selectUserDirectionPointDetail(Long uid, Long pointPoolId, long pageIndex, long pageSize) {
        return userPointDetailDao.selectUserDirectionPointDetail(uid, pointPoolId, pageIndex, pageSize);
    }

    @Override
    public DomainPage selectUserPointDetailP(Long uid, boolean isDirection, long pageIndex, long pageSize) {
        return userPointDetailDao.selectUserPointDetailP(uid, isDirection, pageIndex, pageSize);
    }


}