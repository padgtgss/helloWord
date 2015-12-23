package com.pemass.service.pemass.biz;

import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;

/**
 * @author luoc
 * @Description: ${todo}
 * @Description: ${todo}
 * @date 2014/10/13
 */
public interface ProductSnapshotService {

    /**
     * 保存商品快照
     *
     * @param productSnapshot
     */
    void saveSnapshot(ProductSnapshot productSnapshot);

    /**
     * 更新商品快照
     *
     * @param source
     * @return
     */
    ProductSnapshot updateProductSnapshot(ProductSnapshot source);

    /**
     * 根据ID获取商品快照
     *
     * @param snapshotId
     * @return
     */
    ProductSnapshot getSnapshotById(Long snapshotId);


    /**
     * 保存商品快照
     *
     * @param product
     */
    ProductSnapshot saveSnapshot(Product product);
}
