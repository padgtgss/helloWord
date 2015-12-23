/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.biz.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;
import com.pemass.service.pemass.biz.ProductSnapshotService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: ProductSnapshotService
 * @Author: pokl.huang
 * @CreateTime: 2015-07-09 10:21
 */
@Service
public class ProductSnapshotServiceImpl implements ProductSnapshotService {

    @Resource
    private BaseDao jpaBaseDao;

    @Override
    public void saveSnapshot(ProductSnapshot productSnapshot) {
        Preconditions.checkNotNull(productSnapshot);
        jpaBaseDao.persist(productSnapshot);
    }

    @Override
    public ProductSnapshot updateProductSnapshot(ProductSnapshot source) {
        Preconditions.checkNotNull(source);
        ProductSnapshot targetSnapshot = getSnapshotById(source.getId());
        MergeUtil.merge(source, targetSnapshot);
        return jpaBaseDao.merge(targetSnapshot);
    }

    @Override
    public ProductSnapshot getSnapshotById(Long snapshotId) {
        Preconditions.checkNotNull(snapshotId);
        return jpaBaseDao.getEntityById(ProductSnapshot.class, snapshotId);
    }

    @Override
    public ProductSnapshot saveSnapshot(Product product) {
        //生成商品快照
        ProductSnapshot snapshot = new ProductSnapshot();
        MergeUtil.merge(product, snapshot,new String[]{"id"});
        snapshot.setProductId(product.getId());
        jpaBaseDao.persist(snapshot);
        return snapshot;
    }
}

