package com.pemass.service.pemass.biz;

import com.pemass.persist.domain.jpa.biz.ProductImage;

import java.util.List;

/**
 * @Description: ${todo}
 * @author luoc
 * @date 2014/10/13
 */
public interface ProductImageService {

    /**
     * 添加商品图片
     * @param imageUrl
     * @return
     */
    void addImage(String imageUrl,Long productId);

    /**
     * 得到商品图片列表
     * @param id
     * @return
     */
    List<ProductImage> getProductImageList(Long id);

    /**
     * 更新商品图片
     * @param imageUrl
     * @param id
     */
    void updateProductImage(List<String> imageUrl, Long id);

    /**
     * 更新商品图片
     * @param productImages
     * @param id
     */
    void updateImage(List<ProductImage> productImages, Long id);

    /**
     * 根据图片序列号查询图片
     * @param productId 商品id
     * @return
     */
    List<ProductImage> getProductImageBySequence(Long productId);

}
