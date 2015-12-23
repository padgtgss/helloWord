package com.pemass.service.pemass.poi.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.poi.PointPoolOrganizationDao;
import com.pemass.persist.domain.jpa.poi.PointPoolOrganization;
import com.pemass.service.pemass.poi.PointPoolOrganizationService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Description: PointPurchaseService
 * @Author: cassiel.liu
 * @CreateTime: 2015-07-16 22:12
 */
@Service
public class PointPoolOrganizationServiceImpl implements PointPoolOrganizationService {


    @Resource
    private PointPoolOrganizationDao pointPoolOrganizationDao;

    @Resource
    private BaseDao jpaBaseDao;

    @Override
    public PointPoolOrganization insert(PointPoolOrganization pointPoolOrganization) {
        Preconditions.checkNotNull(pointPoolOrganization);
        jpaBaseDao.persist(pointPoolOrganization);
        return pointPoolOrganization;
    }

    @Override
    public PointPoolOrganization update(PointPoolOrganization source) {
        Preconditions.checkNotNull(source);
        PointPoolOrganization target = selectById(source.getId());
        target.setUpdateTime(DateTime.now().toDate());
        MergeUtil.merge(source, target);
        return jpaBaseDao.merge(target);
    }

    @Override
    public PointPoolOrganization selectById(long pointPoolOrganizationId) {
        Preconditions.checkNotNull(pointPoolOrganizationId);
        return jpaBaseDao.getEntityById(PointPoolOrganization.class, pointPoolOrganizationId);
    }

    @Override
    public PointPoolOrganization deleteById(long pointPoolOrganizationId) {
        Preconditions.checkNotNull(pointPoolOrganizationId);
        PointPoolOrganization target = selectById(pointPoolOrganizationId);
        target.setAvailable(AvailableEnum.UNAVAILABLE);
        target.setUpdateTime(DateTime.now().toDate());
        return pointPoolOrganizationDao.merge(target);
    }


    @Override
    public List<PointPoolOrganization> selectByOrganizationId(Long organizationId) {
        Preconditions.checkNotNull(organizationId);
        @SuppressWarnings("unchecked") List<PointPoolOrganization> pointPoolOrganizations = jpaBaseDao.getEntitiesByField(PointPoolOrganization.class, "organizationId", organizationId);
        return pointPoolOrganizations;
    }

    @Override
    public List<PointPoolOrganization> selectAllPointPoolOrganization() {
        return jpaBaseDao.getAllEntities(PointPoolOrganization.class);
    }

    @Override
    public DomainPage selectAllPointPoolOrganization(long pageIndex, long pageSize) {
        return pointPoolOrganizationDao.selectAllPointPoolOrganization(pageIndex, pageSize);
    }

}
