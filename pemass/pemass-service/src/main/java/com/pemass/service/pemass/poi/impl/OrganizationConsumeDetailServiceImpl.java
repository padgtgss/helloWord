package com.pemass.service.pemass.poi.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.dao.poi.OrganizationConsumeDetailDao;
import com.pemass.persist.domain.jpa.poi.OrganizationConsumeDetail;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.service.pemass.poi.OrganizationConsumeDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: OrganizationConsumeDetailServiceImpl
 * @Author: cassiel.liu
 * @CreateTime: 2015-07-16 22:12
 */
@Service
public class OrganizationConsumeDetailServiceImpl implements OrganizationConsumeDetailService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private OrganizationConsumeDetailDao organizationConsumeDetailDao;

    @Override
    public OrganizationConsumeDetail insert(OrganizationConsumeDetail organizationConsumeDetail) {
        jpaBaseDao.persist(organizationConsumeDetail);
        return organizationConsumeDetail;
    }

    @Override
    public List<OrganizationConsumeDetail> selectByConsumeTypeAndTargetId(ConsumeTypeEnum consumeType, Long consumeTargetId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("consumeTargetId", consumeTargetId);
        map.put("consumeType", consumeType);
        return jpaBaseDao.getEntitiesByFieldList(OrganizationConsumeDetail.class, map);
    }

    @Override
    public boolean updateAmount(ConsumeTypeEnum consumeType, Long consumeTargetId) {
        Preconditions.checkNotNull(consumeType);
        Preconditions.checkNotNull(consumeTargetId);
        return organizationConsumeDetailDao.updateAmount(consumeType, consumeTargetId);
    }

    @Override
    public void deleteConsumeDetail(Long consumeDetailId) {
        OrganizationConsumeDetail organizationConsumeDetail = jpaBaseDao.getEntityById(OrganizationConsumeDetail.class, consumeDetailId);
        organizationConsumeDetail.setAvailable(AvailableEnum.UNAVAILABLE);
        jpaBaseDao.merge(organizationConsumeDetail);
    }

    @Override
    public Integer getCountConsumeAmountByPresentPackId(Long presentPackId, ConsumeTypeEnum consumeType) {
        Preconditions.checkNotNull(presentPackId);
        Preconditions.checkNotNull(consumeType);
        return organizationConsumeDetailDao.getCountConsumeAmountByPresentPackId(presentPackId, consumeType);
    }

    @Override
    public boolean updateByPresentPackId(Integer auditStatus, ConsumeTypeEnum consumeType, Long presentPackId) {
        Preconditions.checkNotNull(consumeType);
        Preconditions.checkNotNull(presentPackId);
        return organizationConsumeDetailDao.updateByPresentPackId(auditStatus, consumeType, presentPackId);
    }

    @Override
    public Integer getUnsuccessfulConsumeAmountByPointPoolId(Long pointPoolId) {
        Preconditions.checkNotNull(pointPoolId);
        return organizationConsumeDetailDao.getPointUnsuccessfulConsumeAmount(pointPoolId);
    }

}
