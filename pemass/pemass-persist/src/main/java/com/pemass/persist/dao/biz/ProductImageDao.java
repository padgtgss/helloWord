package com.pemass.persist.dao.biz;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.biz.ProductImage;

import java.util.List;

/**
 * Created by Administrator on 2015-4-28.
 */
public interface ProductImageDao extends BaseDao {

    /**
     * 根据图片序列号查询图片
     * @param productId 商品id
     * @return
     */
    List<ProductImage> getProductImageBySequence(Long productId);
}
