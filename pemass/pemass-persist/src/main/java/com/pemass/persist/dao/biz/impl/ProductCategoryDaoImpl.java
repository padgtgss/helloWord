/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.biz.ProductCategoryDao;
import com.pemass.persist.domain.jpa.biz.ProductCategory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * @Description: ProductCategoryDaoImpl
 * @Author: Administrator
 * @CreateTime: 2015-04-27 11:17
 */
@Repository
public class ProductCategoryDaoImpl extends JPABaseDaoImpl implements ProductCategoryDao {
    @Override
    public List<ProductCategory> getSecondLevelCategory() {
        String sql = "select * from biz_product_category where parent_category_id != 0 and available = 1 and state = 1";

        Query query = em.createNativeQuery(sql,ProductCategory.class);

        List<ProductCategory> list = query.getResultList();
        return list;
    }
}