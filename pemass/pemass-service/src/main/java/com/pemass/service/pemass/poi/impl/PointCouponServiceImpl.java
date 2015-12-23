/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.poi.impl;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.poi.PointCouponDao;
import com.pemass.persist.domain.jpa.poi.OrganizationConsumeDetail;
import com.pemass.persist.domain.jpa.poi.PointCoupon;
import com.pemass.persist.domain.jpa.poi.PointPurchase;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.service.exception.PoiError;
import com.pemass.service.pemass.poi.PointCouponService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: PointCouponServiceImpl
 * @Author: cassiel.liu
 * @CreateTime: 2015-05-05 10:23
 */
@Service
public class PointCouponServiceImpl implements PointCouponService {


    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private PointCouponDao pointCouponDao;

//    @Resource
//    private UserPointDetailService detailService;


    @Override
    public DomainPage selectCouponByGroup(Map<String, Object> preciseMap, Map<String, Object> fuzzyMap, long pageIndex, long pageSize) {
        DomainPage domainPage = pointCouponDao.getCouponByGroup(preciseMap, fuzzyMap, pageIndex, pageSize);
        List list = new ArrayList();
        List result = domainPage.getDomains();
        for (int i = 0; i < domainPage.getDomains().size(); i++) {
            Object[] objects = new Object[3];
            Object[] obj = (Object[]) result.get(i);
            PointCoupon pointCoupon = (PointCoupon) obj[0];
            objects[0] = pointCoupon;
            objects[1] = obj[1];

            //根据批次号获取该批次的总数
            Long count = pointCouponDao.getCountCouponByIdentifier(pointCoupon.getPackIdentifier());
            objects[2] = count;
            //将数组添加到list集合中
            list.add(objects);
        }
        domainPage.setDomains(list);
        return domainPage;
    }

    @Override
    public DomainPage selectCouponByIdentifier(Map<String, Object> preciseMap, Map<String, Object> fuzzyMap, long pageIndex, long pageSize) {
        return pointCouponDao.getCouponByIdentifier(preciseMap, fuzzyMap, pageIndex, pageSize);
    }

    @Override
    public Boolean insert(PointCoupon pointCoupon, Integer amount) {
        /**扣除商户积分*/
        PointPurchase pointPurchase = jpaBaseDao.getEntityById(PointPurchase.class, pointCoupon.getPointPurchaseId());
        Integer toolAmount = amount * pointCoupon.getAmount();
        updateOrganizationPoint(pointPurchase, toolAmount);

        /**添加积分券信息*/
        //获取批号
        String identifier = this.getPointCouponIdentifier();
        List<PointCoupon> pointCouponList = jpaBaseDao.getAllEntities(PointCoupon.class);
        String card = null;
        Boolean bool = false; //卡密是否存在，true存在，false不存在
        for (int i = 0; i < amount; i++) {
            //获取卡密
            String cardSecret = this.getCardSecret();
            //循环取出来的所有积分券信息
            for (int j = 1; j <= pointCouponList.size(); j++) {
                for (int k = 0; k < j; k++) {
                    card = pointCouponList.get(k).getCardSecret();
                    //判断当前卡密是否和取出来的卡密相等，如果相等，则重新生成
                    if (cardSecret.equals(card)) {
                        cardSecret = this.getCardSecret();
                        bool = true;
                        j--;
                    } else {
                        bool = false;
                    }
                }
            }
            if (bool == false) {
                pointCoupon.setCardSecret(cardSecret);
            }
            pointCoupon.setPackIdentifier(identifier);
            PointCoupon coupon = new PointCoupon();
            MergeUtil.merge(pointCoupon, coupon);
            jpaBaseDao.persist(coupon);
            //根据
            PointCoupon newPointCoupon = jpaBaseDao.getEntityByField(PointCoupon.class, "cardSecret", pointCoupon.getCardSecret());

            /**添加商户积分消耗记录*/
            insertOrganizationConsumeDetail(newPointCoupon.getId(), pointPurchase, newPointCoupon.getPointType(), newPointCoupon.getAmount());
        }
        return true;
    }

    @Override
    public String getPointCouponIdentifier() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String nowStr = sdf.format(now);

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dataStr = sdf.format(now);

        String number = pointCouponDao.getMaxIdentifier(dataStr);
        return nowStr + number;
    }

    @Override
    public String getCardSecret() {
        Random ran = new Random();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            if (i % 2 == 0) { //生成小写字母
                byte a = 97;          // 97+26位随机数，小写字母byte范围
                int random = a + ran.nextInt(26);
                result.append(String.valueOf((char) (byte) random));
            } else {  //生成数字
                result.append(String.valueOf(ran.nextInt(10)));
            }
        }
        return result.toString();
    }

    /**
     * 添加商家积分消耗明细
     *
     * @param id            消耗对象的ID
     * @param purchase      积分认购对象
     * @param pointTypeEnum 积分类型
     * @param useableAmount 消耗数量
     */
    private void insertOrganizationConsumeDetail(Long id, PointPurchase purchase, PointTypeEnum pointTypeEnum, Integer useableAmount) {
        OrganizationConsumeDetail organizationConsumeDetail = new OrganizationConsumeDetail();
        organizationConsumeDetail.setConsumeType(ConsumeTypeEnum.POINT_COUPON);
        organizationConsumeDetail.setPointType(pointTypeEnum);
        organizationConsumeDetail.setConsumeTargetId(id);
        organizationConsumeDetail.setPointPurchaseId(purchase.getId());
        organizationConsumeDetail.setAmount(useableAmount);

        jpaBaseDao.persist(organizationConsumeDetail);
    }

    /**
     * 更新商家积分
     *
     * @param purchase 认购积分对象
     * @param amount   需要扣除的积分数量
     */
    private void updateOrganizationPoint(PointPurchase purchase, Integer amount) {
        PointPurchase pointPurchase = jpaBaseDao.getEntityById(PointPurchase.class, purchase.getId());
//        pointPurchase.setUseableAmount(pointPurchase.getUseableAmount() - amount);
        jpaBaseDao.merge(pointPurchase);
    }

    @Override
    @Transactional
    public Boolean couponRecharge(String packIdentifier, String cardSecret) {
        PointCoupon coupon = pointCouponDao.selectCouponByFileds(packIdentifier, cardSecret);
        if(coupon == null){
            throw new BaseBusinessException(PoiError.POINT_COUPON_NOT_EXIST);
        }
        PointPurchase pointPurchase = jpaBaseDao.getEntityById(PointPurchase.class, coupon.getPointPurchaseId());
        if ( coupon.getExpiryTime().after(new Date())
                && pointPurchase.getExpiryTime().after(new Date())
                && OrderItemStatusEnum.UNUSED.equals(coupon.getUseStatus())) {
            //生成积分明细
//            detailService.insertPointDetail(coupon);
            //更改积分券使用状态
            coupon.setUseTime(new Date());
            coupon.setUseStatus(OrderItemStatusEnum.USED);
            jpaBaseDao.merge(coupon);
            return true;
        } else {
            throw new BaseBusinessException(PoiError.POINT_COUPON_NOT_EXIST);
        }

    }
}
