package com.pemass.service.pemass.biz.impl;

import com.google.common.collect.Lists;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.biz.ProductApplyRecordDao;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.biz.ProductApplyRecord;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.service.pemass.biz.ProductApplyRecordService;
import com.pemass.service.pemass.biz.ProductService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: ProductApplyRecordServiceImpl
 * @Author: luoc
 * @CreateTime: 2015-06-01 15:40
 */
@Service
public class ProductApplyRecordServiceImpl implements ProductApplyRecordService {
    @Resource
    private ProductApplyRecordDao productApplyRecordDao;

    @Resource
    private ProductService productService;

    @Resource
    private OrganizationService organizationService;

    @Override
    public DomainPage selectApplyRecordList(Long organizationId,String productName,long pageIndex,long pageSize) {
        DomainPage domainPage = productApplyRecordDao.selectApplyRecordByConditions(organizationId,productName,pageIndex,pageSize);
        List<Object []> list = Lists.newArrayList();
        if(domainPage.getDomains() != null && domainPage.getDomains().size() > 0){
            for(int i = 0;i < domainPage.getDomains().size();i++){
                Object[] objects = (Object[]) domainPage.getDomains().get(i);
                long productApplyRecordId = new BigInteger(objects[0].toString()).longValue();
                long productId = new BigInteger(objects[1].toString()).longValue();
                Object [] newObjects = new Object[3];
                newObjects[0] = productApplyRecordDao.getEntityById(ProductApplyRecord.class,productApplyRecordId);
                newObjects[1] = productApplyRecordDao.getEntityById(Product.class,productId);
                newObjects[2] = productApplyRecordDao.getEntityById(Product.class,((ProductApplyRecord)newObjects[0]).getParentProductId());
                list.add(newObjects);
            }
        }
        domainPage.setDomains(list);
        return domainPage;
    }

    @Override
    public ProductApplyRecord saveRecord(ProductApplyRecord record) {
        return productApplyRecordDao.merge(record);
    }

    @Override
    public DomainPage selectApplyRecordListByConditions(Map<String, Object> conditions, DomainPage domainPage) {
        DomainPage dp = productApplyRecordDao.selectApplyRecordByConditions(conditions,domainPage);
        List<Object[]> list = Lists.newArrayList();
        if(dp.getDomains() != null && dp.getDomains().size() > 0){
            for(int i = 0;i < dp.getDomains().size();i++){
                long productApplyRecordId = new BigInteger(((Object)dp.getDomains().get(i)).toString()).longValue();
                Object [] newObjects = new Object[3];
                newObjects[0] = productApplyRecordDao.getEntityById(ProductApplyRecord.class,productApplyRecordId);
                long productId = new BigInteger(((ProductApplyRecord)newObjects[0]).getParentProductId().toString()).longValue();
                long oId = new BigInteger(((ProductApplyRecord)newObjects[0]).getOrganizationId().toString()).longValue();
                newObjects[1] = productService.getProductInfo(productId);
                newObjects[2] = organizationService.getOrganizationById(oId);
                list.add(newObjects);
            }
        }
        dp.setDomains(list);
        return dp;
    }

    @Override
    public void auditProductApply(ProductApplyRecord productApply) {
        // 修改申请
        ProductApplyRecord targetProductApply = productApplyRecordDao.getEntityById(ProductApplyRecord.class, productApply.getId());
        MergeUtil.merge(productApply, targetProductApply);
        productApplyRecordDao.merge(targetProductApply);
        // 修改商品 - 通过才修改商品
        if(productApply.getAuditStatus() == AuditStatusEnum.HAS_AUDIT){
            Product product = productService.getProductInfo(targetProductApply.getProductId());
            product.setBasePrice(targetProductApply.getActualApplyPrice());
            product.setUpdateTime(new Date());
            productApplyRecordDao.merge(product);
        }
    }

    @Override
    public List<ProductApplyRecord> getProductApplyRecordList(Map<String, Object> fieldMap) {
        return productApplyRecordDao.getEntitiesByFieldList(ProductApplyRecord.class, fieldMap);
    }
}
