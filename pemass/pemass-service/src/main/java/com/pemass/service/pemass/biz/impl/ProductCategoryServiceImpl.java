package com.pemass.service.pemass.biz.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.biz.ProductCategoryDao;
import com.pemass.persist.domain.jpa.biz.ProductCategory;
import com.pemass.service.pemass.biz.ProductCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: ProductCategoryServiceImpl
 * @Author: vahoa.ma
 * @CreateTime: 2015-04-23 16:12
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Resource
    private ProductCategoryDao productCategoryDao;


    @Override
    public void saveProductCategory(ProductCategory productCategory) {
        Preconditions.checkNotNull(productCategory);
        productCategoryDao.persist(productCategory);
    }

    @Override
    public ProductCategory updateProductCategory(ProductCategory source) {
        Preconditions.checkNotNull(source);
        ProductCategory targetCategory = selectProductCategoryById(source.getId());
        MergeUtil.merge(source, targetCategory);
        return productCategoryDao.merge(targetCategory);
    }

    @Override
    public ProductCategory selectProductCategoryById(Long productCategoryId) {
        Preconditions.checkNotNull(productCategoryId);
        return productCategoryDao.getEntityById(ProductCategory.class, productCategoryId);
    }

    @Override
    public ProductCategory deleteProductCategoryById(Long productCategoryId) {
        Preconditions.checkNotNull(productCategoryId);
        ProductCategory targetCategory = selectProductCategoryById(productCategoryId);
        targetCategory.setAvailable(AvailableEnum.UNAVAILABLE);
        targetCategory.setUpdateTime(new Date());
        return productCategoryDao.merge(targetCategory);
    }

    @Override
    public DomainPage querySecondType(Map<String, Object> fieldNameValueMap, long pageIndex, long pageSize) {
        return productCategoryDao.getEntitiesPagesByFieldList(ProductCategory.class,fieldNameValueMap,pageIndex,pageSize);
    }

    @Override
    public ProductCategory insert(ProductCategory productCategory) {
        productCategoryDao.persist(productCategory);
        return productCategory;
    }

    @Override
    public ProductCategory selectById(Long id) {
        return productCategoryDao.getEntityById(ProductCategory.class, id);
    }

    @Override
    public ProductCategory update(ProductCategory productCategory) {
        ProductCategory storedProductCategory = productCategoryDao.getEntityById(ProductCategory.class, productCategory.getId());
        /**合并实体*/
        MergeUtil.merge(productCategory, storedProductCategory);

        productCategoryDao.merge(storedProductCategory);
        return storedProductCategory;
    }

    @Override
    public List<ProductCategory> getPrimaryCategories() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("parentCategoryId", 0L);
        return productCategoryDao.getEntitiesByFieldList(ProductCategory.class, map);
    }

    @Override
    public List<ProductCategory> getSecondLevelCategory() {
        return productCategoryDao.getSecondLevelCategory();
    }

    @Override
    public List<ProductCategory> getSecondLevelCategory(Long id) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("parentCategoryId", id);
        return productCategoryDao.getEntitiesByFieldList(ProductCategory.class, map);
    }


    @Override
    public ProductCategory getProductCategoryById(Long id) {
        return productCategoryDao.getEntityById(ProductCategory.class,id);
    }

    @Override
    public Map<String,Object> isCreateTicket(ProductCategory productCategory) {
        if(productCategory != null){
            productCategory = productCategoryDao.getEntityById(ProductCategory.class,productCategory.getId());
            Map<String,Object> map = Maps.newHashMap();
            map.put("isCreateTicket",productCategory.getIsCreateTicket());
            map.put("isDistribution",productCategory.getIsDistribution());
            return map;
        }else{
           return null;
        }
    }


}
