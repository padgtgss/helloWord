/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.dao.poi.OrganizationPointDetailDao;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;
import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.persist.domain.jpa.poi.OrganizationPointDetail;
import com.pemass.persist.domain.jpa.poi.PointPurchase;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.enumeration.PointChannelEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.service.exception.PoiError;
import com.pemass.service.pemass.biz.ProductService;
import com.pemass.service.pemass.biz.ProductSnapshotService;
import com.pemass.service.pemass.poi.OrganizationPointDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: OrganzationPointDetailServiceImpl
 * @Author: pokl.huang
 * @CreateTime: 2015-07-15 10:30
 */
@Service
public class OrganzationPointDetailServiceImpl implements OrganizationPointDetailService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private OrganizationPointDetailDao organizationPointDetailDao;

    @Resource
    private ProductSnapshotService productSnapshotService;

    @Resource
    private ProductService productService;


    @Override
    public void updateOrganizationPointE(Organization organization, OrderItem orderItem, User purchaseUser) {
        List<OrganizationPointDetail> organizationPointDetailList = jpaBaseDao.getEntitiesByField(OrganizationPointDetail.class, "organizationId", organization.getId());

        ProductSnapshot snapshot = productSnapshotService.getSnapshotById(orderItem.getProductSnapshotId());
        Product product = productService.getProductInfo(snapshot.getProductId());
        Integer GivingEnumber = product.getGivingEnumber();
        for (int i = 0; i < organizationPointDetailList.size(); i++) {
            OrganizationPointDetail organizationPointDetail = organizationPointDetailList.get(i);
            if ((organizationPointDetail.getPointType().equals(PointTypeEnum.E))
                    && (organizationPointDetail.getExpiryTime().after(new Date()))
                    && (GivingEnumber > 0)) {

//                if (GivingEnumber > pointPurchase.getUseableAmount())//商品所赠送积分数 大于 该条记录可用积分数
//                {
//                    Integer useableAmount = pointPurchase.getUseableAmount();
//                    addPointE(purchaseUser, organization, pointPurchase.getUseableAmount(), pointPurchase.getExpiryTime(), pointPurchase);//用户获取积分
//                    GivingEnumber = GivingEnumber - pointPurchase.getUseableAmount();
//                    pointPurchase.setUseableAmount(0);
//                    orderDao.merge(pointPurchase);
//
//                    OrganizationConsumeDetail organizationConsumeDetail = new OrganizationConsumeDetail();
//                    organizationConsumeDetail.setPointPurchaseId(pointPurchase.getId());
//                    organizationConsumeDetail.setConsumeType(ConsumeTypeEnum.ORDER);
//                    organizationConsumeDetail.setConsumeTargetId(orderItem.getOrderId());
//                    organizationConsumeDetail.setPointType(PointTypeEnum.E);
//                    organizationConsumeDetail.setAmount(useableAmount);
//                    orderDao.persist(organizationConsumeDetail);
//                } else {//商品所赠送积分数 小于等于 该条记录可用积分数
//                    addPointE(purchaseUser, organization, GivingEnumber, pointPurchase.getExpiryTime(), pointPurchase);//用户获取积分
//                    pointPurchase.setUseableAmount(pointPurchase.getUseableAmount() - GivingEnumber);
//                    orderDao.merge(pointPurchase);
//
//                    OrganizationConsumeDetail organizationConsumeDetail = new OrganizationConsumeDetail();
//                    organizationConsumeDetail.setPointPurchaseId(pointPurchase.getId());
//                    organizationConsumeDetail.setConsumeType(ConsumeTypeEnum.ORDER);
//                    organizationConsumeDetail.setConsumeTargetId(orderItem.getOrderId());
//                    organizationConsumeDetail.setPointType(PointTypeEnum.E);
//                    organizationConsumeDetail.setAmount(GivingEnumber);
//                    orderDao.persist(organizationConsumeDetail);
//
//                    //赠送完贝积分返回之前给用户发送推送消息
//                    this.pointChangePushMessage(purchaseUser, PointTypeEnum.E, "INCOME", product.getGivingEnumber());
//                    return;
//                }
            }
        }
        /**1,如何积分不够赠送，则抛出异常，回退所有相关数据*/
        if (GivingEnumber > 0) {
            throw new BaseBusinessException(PoiError.INTEGRAL_NOT_USE);
        }
    }

    @Override
    public OrganizationPointDetail selectByPurchaseId(Long pointPurchaseId) {
        return jpaBaseDao.getEntityByField(OrganizationPointDetail.class,"pointPurchaseId",pointPurchaseId);
    }

    @Override
    public OrganizationPointDetail insert(OrganizationPointDetail organizationPointDetail) {
        jpaBaseDao.persist(organizationPointDetail);
        return organizationPointDetail;
    }

    @Override
    public OrganizationPointDetail insertByPurchase(PointPurchase pointPurchase) {
        OrganizationPointDetail organizationPointDetail = new OrganizationPointDetail();
        organizationPointDetail.setPointPurchaseId(pointPurchase.getId());
        organizationPointDetail.setOrganizationId(pointPurchase.getOrganizationId());
        organizationPointDetail.setPointType(pointPurchase.getPointType());
        organizationPointDetail.setArea(pointPurchase.getArea());
        organizationPointDetail.setAmount(pointPurchase.getAmount());
        organizationPointDetail.setUseableAmount(pointPurchase.getAmount());
        organizationPointDetail.setPointChannel(PointChannelEnum.ORGANIZATION_SUBSCRIBE);
        organizationPointDetail.setExpiryTime(pointPurchase.getExpiryTime());
        jpaBaseDao.persist(organizationPointDetail);
        return organizationPointDetail;
    }

    @Override
    public OrganizationPointDetail selectById(Long id) {
        Preconditions.checkNotNull(id);
        return organizationPointDetailDao.getEntityById(OrganizationPointDetail.class, id);
    }

    @Override
    public Long getUseableAmount(Map<String, Object> fieldNameValueMap) {
        return organizationPointDetailDao.getSumUseableAmount(fieldNameValueMap);
    }

    @Override
    public Integer getPointAmountByPointPoolId(Long pointPoolId) {
        Preconditions.checkNotNull(pointPoolId);
        return organizationPointDetailDao.getAmountByPointPoolId(pointPoolId);
    }

    @Override
    public void updateOrganizationPointP(Organization organization, OrderItem orderItem, User purchaseUser) {
        List<OrganizationPointDetail> organizationPointDetailList = jpaBaseDao.getEntitiesByField(OrganizationPointDetail.class, "organizationId", organization.getId());

        ProductSnapshot snapshot = productSnapshotService.getSnapshotById(orderItem.getProductSnapshotId());
        Product product = productService.getProductInfo(snapshot.getProductId());
        Integer GivingPnumber = product.getGivingPnumber();
        for (int i = 0; i < organizationPointDetailList.size(); i++) {
            OrganizationPointDetail organizationPointDetail = organizationPointDetailList.get(i);
            if (organizationPointDetail.getPointType().equals(PointTypeEnum.P) && (organizationPointDetail.getExpiryTime().after(new Date())) && (GivingPnumber > 0)) {
//                if (GivingPnumber > pointPurchase.getUseableAmount())//商品所赠送积分数 大于 该条记录可用积分数
//                {
//                    Integer useableAmount = pointPurchase.getUseableAmount();
//                    addPointP(purchaseUser, organization, pointPurchase.getUseableAmount(), pointPurchase.getExpiryTime(), pointPurchase);//用户获取积分
//                    GivingPnumber = GivingPnumber - pointPurchase.getUseableAmount();
//                    pointPurchase.setUseableAmount(0);
//                    orderDao.merge(pointPurchase);
//
//                    OrganizationConsumeDetail organizationConsumeDetail = new OrganizationConsumeDetail();
//                    organizationConsumeDetail.setPointPurchaseId(pointPurchase.getId());
//                    organizationConsumeDetail.setConsumeType(ConsumeTypeEnum.ORDER);
//                    organizationConsumeDetail.setConsumeTargetId(orderItem.getOrderId());
//                    organizationConsumeDetail.setPointType(PointTypeEnum.P);
//                    organizationConsumeDetail.setAmount(useableAmount);
//                    orderDao.persist(organizationConsumeDetail);
//                } else {//商品所赠送积分数 小于等于 该条记录可用积分数
//                    addPointP(purchaseUser, organization, GivingPnumber, pointPurchase.getExpiryTime(), pointPurchase);//用户获取积分
//                    pointPurchase.setUseableAmount(pointPurchase.getUseableAmount() - GivingPnumber);
//                    orderDao.merge(pointPurchase);
//
//                    OrganizationConsumeDetail organizationConsumeDetail = new OrganizationConsumeDetail();
//                    organizationConsumeDetail.setPointPurchaseId(pointPurchase.getId());
//                    organizationConsumeDetail.setConsumeType(ConsumeTypeEnum.ORDER);
//                    organizationConsumeDetail.setConsumeTargetId(orderItem.getOrderId());
//                    organizationConsumeDetail.setPointType(PointTypeEnum.P);
//                    organizationConsumeDetail.setAmount(GivingPnumber);
//                    orderDao.persist(organizationConsumeDetail);
//
//                    //赠送完派积分返回之前给用户发送推送消息
//                    this.pointChangePushMessage(purchaseUser, PointTypeEnum.P, "INCOME", product.getGivingEnumber());
//
//                    return;
//                }
            }
        }
        /**1,如何积分不够赠送，则抛出异常，回退所有相关数据*/
        if (GivingPnumber > 0) {
            throw new BaseBusinessException(PoiError.INTEGRAL_NOT_USE);
        }
    }
}

