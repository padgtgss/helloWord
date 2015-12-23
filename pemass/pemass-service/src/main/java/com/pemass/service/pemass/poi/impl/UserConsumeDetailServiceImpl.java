/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.dao.poi.UserConsumeDetailDao;
import com.pemass.persist.domain.jpa.poi.UserConsumeDetail;
import com.pemass.persist.domain.jpa.poi.UserPointDetail;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.service.pemass.poi.UserConsumeDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: UserConsumeDetailServiceImpl
 * @Author: pokl.huang
 * @CreateTime: 2015-07-17 15:25
 */
@Service
public class UserConsumeDetailServiceImpl implements UserConsumeDetailService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private UserConsumeDetailDao userConsumeDetailDao;

    @Override
    public boolean updateAmount(ConsumeTypeEnum consumeType, Long consumeTargetId) {
        Preconditions.checkNotNull(consumeType);
        Preconditions.checkNotNull(consumeTargetId);
        return userConsumeDetailDao.updateAmount(consumeType, consumeTargetId);
    }

    @Override
    public void insertPointConsumeDetail(UserPointDetail userPointDetail, Long poolId) {
        UserConsumeDetail userConsumeDetail = new UserConsumeDetail();
        userConsumeDetail.setConsumeTargetId(0L);
        userConsumeDetail.setConsumeType(ConsumeTypeEnum.INTEGRAL_OVERDUE_RECOVERY);
        userConsumeDetail.setPointType(PointTypeEnum.P);
        userConsumeDetail.setAmount(userPointDetail.getUseableAmount());
        userConsumeDetail.setPayableAmount(userPointDetail.getUseableAmount());
        userConsumeDetail.setUserPointDetailId(userPointDetail.getId());
        jpaBaseDao.persist(userConsumeDetail);
    }

    @Override
    public List<UserConsumeDetail> selectByConsumeTypeAndTargetId(ConsumeTypeEnum consumeType, Long consumeTargetId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("consumeTargetId", consumeTargetId);
        map.put("consumeType", consumeType);
        return jpaBaseDao.getEntitiesByFieldList(UserConsumeDetail.class, map);
    }

    @Override
    public Integer getPointConsumeAmountByPointPoolId(Long pointPoolId) {
        Preconditions.checkNotNull(pointPoolId);
        return userConsumeDetailDao.getPointConsumeAmountByPointPoolId(pointPoolId);
    }

    @Override
    public Integer getCongelationAmount(Long uid, PointTypeEnum pointType, Boolean isGeneral) {
        return userConsumeDetailDao.getCongelaTionAmount(uid,pointType,isGeneral);
    }

    @Override
    public List<UserConsumeDetail> getUserConsumeDetailListByOrderId(Long orderId,ConsumeTypeEnum consumeType,PointTypeEnum pointType) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("consumeType",consumeType);
        map.put("consumeTargetId",orderId);
        map.put("pointType",pointType);
        return jpaBaseDao.getEntitiesByFieldList(UserConsumeDetail.class,map);
    }
}

