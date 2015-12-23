/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.biz.ProductImageDao;
import com.pemass.persist.domain.jpa.biz.ProductImage;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * @Description: ProductImageDaoImpl
 * @Author: Administrator
 * @CreateTime: 2015-04-28 10:00
*/

@Repository
public class ProductImageDaoImpl extends JPABaseDaoImpl implements ProductImageDao {
    @Override
    public List<ProductImage> getProductImageBySequence(Long productId) {

        String sql = "select * from biz_product_image where product_id = "+ productId +" and available = 1 ORDER BY sequence";
        Query query = em.createNativeQuery(sql,ProductImage.class);

        return query.getResultList();
    }
}