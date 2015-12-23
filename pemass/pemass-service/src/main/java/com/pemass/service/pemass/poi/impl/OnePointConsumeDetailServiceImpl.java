/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.persist.dao.poi.OnePointConsumeDetailDao;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.poi.OnePointConsumeDetail;
import com.pemass.persist.domain.jpa.poi.OnePointDetail;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PayStatusEnum;
import com.pemass.pojo.poi.OnePointConsumeDetailPojo;
import com.pemass.service.pemass.poi.OnePointConsumeDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: OnePointConsumeDetailServiceImpl
 * @Author: lin.shi
 * @CreateTime: 2015-06-11 21:48
 */
@Service
public class OnePointConsumeDetailServiceImpl implements OnePointConsumeDetailService {
    @Resource
    private OnePointConsumeDetailDao onePointConsumeDetailDao;

    @Resource
    private BaseDao jpaBaseDao;



    @Override
    public DomainPage consumeDetail(Long uid, Long pageIndex, Long pageSize) {
        //1.获取当前用户的所有壹购订单
        List<Expression> list = new ArrayList<Expression>();
        list.add(new Expression("userId", Operation.Equal,uid));
        list.add(new Expression("totalPointO",Operation.GreaterThan,new Integer(0)));
        list.add(new Expression("payStatus",Operation.Equal, PayStatusEnum.HAS_PAY));
        DomainPage<Order> orderDomainPage=jpaBaseDao.getEntitiesPagesByExpressionList(Order.class, list, pageIndex, pageSize);

        DomainPage<OnePointConsumeDetailPojo> domainPage = new DomainPage<OnePointConsumeDetailPojo>();

        domainPage.setTotalCount(orderDomainPage.getTotalCount());
        domainPage.setPageCount(orderDomainPage.getPageCount());
        domainPage.setPageIndex(orderDomainPage.getPageIndex());
        domainPage.setPageSize(orderDomainPage.getPageSize());

        List<OnePointConsumeDetailPojo> onePointConsumeDetailPojoList = new ArrayList<OnePointConsumeDetailPojo>();
        OnePointConsumeDetailPojo pojo = null;
        for(Order order : orderDomainPage.getDomains()){
            pojo = new OnePointConsumeDetailPojo();
            pojo.setOrderIdentifier(order.getOrderIdentifier());
            pojo.setCreateTime(order.getCreateTime());

            List<Object[]> objList =  onePointConsumeDetailDao.consumeDetailCountByOrder(uid,order.getId());
            for(Object[] objects : objList){
                pojo.setMyselfPointCount(Long.valueOf(objects[0].toString()));
                pojo.setFriendPointCount(Long.valueOf(objects[1].toString()));
            }
            onePointConsumeDetailPojoList.add(pojo);
        }
        domainPage.setDomains(onePointConsumeDetailPojoList);
        return domainPage;
    }

    @Override
    public Double getPointCounsumeDetailCount(Long uid){

        return onePointConsumeDetailDao.getConsumeDetailCout(uid);
    }

    @Override
    public Object[] getProfitCount(Long uid){
        return onePointConsumeDetailDao.getProfitCount(uid);
    }

    @Override
    public DomainPage selectProfitDetails(long uid,boolean isYouself,long pageIndex,long pageSize){

        return onePointConsumeDetailDao.selectProfitDetails(uid,isYouself, pageIndex, pageSize);
    }

    @Override
    public DomainPage selectForFriendDetail(Long uid, String belongUserName, long pageIndex, long pageSize) {
        return onePointConsumeDetailDao.selectForFriendDetail(uid,belongUserName,pageIndex,pageSize);
    }

    @Override
    public void insertDetail(OnePointDetail onePointDetail,Order order,Integer amount){
        OnePointConsumeDetail detail = new OnePointConsumeDetail();
        detail.setAmount(0);
        detail.setBelongUserId(onePointDetail.getBelongUserId());
        detail.setUserId(order.getUserId());
        detail.setConsumeType(ConsumeTypeEnum.ORDER);
        detail.setOnePointDetailId(onePointDetail.getId());
        detail.setOrderId(order.getId());
        detail.setPayableAmount(amount);
        detail.setOnePointDetailId(onePointDetail.getId());
        detail.setOrderIdentifier(order.getOrderIdentifier());

        onePointConsumeDetailDao.persist(detail);
    }

    @Override
    public List<OnePointConsumeDetail> selectByOrderId(Long orderId) {
        return jpaBaseDao.getEntitiesByField(OnePointConsumeDetail.class,"orderId",orderId);
    }

    @Override
    public Long getFreeGivingPointAmount(Long uid) {

        return onePointConsumeDetailDao.getFreeGivingPointAmount(uid);
    }
}