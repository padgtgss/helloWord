package com.pemass.service.pemass.biz.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.common.server.domain.Expression;
import com.pemass.persist.domain.jpa.biz.AuditRecord;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.serializer.ObjectIdSerializer;
import com.pemass.service.pemass.bas.ProvinceService;
import com.pemass.service.pemass.biz.AuditRecordService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/14.
 */
@Service
public class AuditRecordServiceImpl extends JPABaseDaoImpl implements AuditRecordService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private ProvinceService provinceService;

    @Override
    public void addOneProductAuditRecord(Product product) {
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setApplyTime(new Date());
        auditRecord.setAuditType(1);
        auditRecord.setOneAuditStatus(1);
        auditRecord.setProductId(product.getId());
        auditRecord.setOrganizationId(product.getOrganizationId());
        jpaBaseDao.persist(auditRecord);
    }

    @Override
    public AuditRecord selectAuditRecordByProductId(Long productId) {
        return jpaBaseDao.getEntityByField(AuditRecord.class,"productId",productId);
    }

    @Override
    public Integer isThereAuditRecord(Long productId) {
        Map<String,Object> map = Maps.newHashMap();
        map.put("productId",productId);
       DomainPage domainPage = jpaBaseDao.getEntitiesPagesByFieldList(AuditRecord.class,map, 1L, (long) Integer.MAX_VALUE);
        if(domainPage.getDomains() == null || domainPage.getDomains().size() == 0){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public void addOnePayAuditRecord(Long oId) {
        Organization organization = jpaBaseDao.getEntityById(Organization.class,oId);
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setOrganizationId(organization.getId());
        auditRecord.setOneAuditStatus(1);
        auditRecord.setAuditType(3);
        auditRecord.setApplyTime(new Date());
        auditRecord.setOrganizationName(organization.getOrganizationName());
        jpaBaseDao.persist(auditRecord);
    }

    @Override
    public DomainPage getAuditRecordByPage(List<Expression> list, long pageIndex, long pageSize) {
        DomainPage domainPage = jpaBaseDao.getEntitiesPagesByExpressionList(AuditRecord.class, list, pageIndex, pageSize);
        List newArrayList = Lists.newArrayList();
        if(domainPage.getDomains() != null && domainPage.getDomains().size() > 0){
            for(int i = 0;i < domainPage.getDomains().size();i++){
                Object [] objects = new Object[3];
                objects[0] = domainPage.getDomains().get(i);
                objects[1] = organizationService.getOrganizationById(((AuditRecord) objects[0]).getOrganizationId());
                objects[2] = provinceService.getProvinceByID(((Organization) objects[1]).getProvinceId());
                newArrayList.add(objects);
            }
        }
        domainPage.setDomains(newArrayList);
        return domainPage;
    }

    @Override
    public AuditRecord getAuditRecordById(Long auditRecordId)
    {
        return jpaBaseDao.getEntityById(AuditRecord.class,auditRecordId);
    }

    @Override
    public void updateAuditRecordInfo(AuditRecord auditRecord) {
        jpaBaseDao.merge(auditRecord);
    }
}
