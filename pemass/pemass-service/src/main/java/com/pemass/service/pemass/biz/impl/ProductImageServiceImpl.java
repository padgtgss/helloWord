package com.pemass.service.pemass.biz.impl;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.dao.biz.ProductImageDao;
import com.pemass.persist.domain.jpa.biz.ProductImage;
import com.pemass.service.pemass.biz.ProductImageService;
import com.pemass.service.pemass.biz.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: ProductImageServiceImpl
 * @Author: luoc
 * @CreateTime: 2014-10-13 10:49
 */
@Service
public class ProductImageServiceImpl implements ProductImageService {
    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private ProductService productService;

    @Resource
    private ProductImageDao productImageDao;

    /**
     * 添加商品图片
     *
     * @param imageUrl
     * @return
     */
    @Override
    public void addImage(String imageUrl, Long productId) {
        double sequence = 0;
        if (imageUrl != null && imageUrl.length() > 0) {
            String[] images = imageUrl.split(",");
            for (int i = 0; i < images.length; i++) {
                sequence++;
                ProductImage productImage = new ProductImage();
                productImage.setUrl(images[i].trim());
                productImage.setSequence(sequence);
                productImage.setProductId(productId);
                jpaBaseDao.persist(productImage);
            }
        }
    }

    /**
     * 得到商品图片列表
     *
     * @param id
     * @return
     */
    @Override
    public List<ProductImage> getProductImageList(Long id) {
        List<ProductImage> productImages = jpaBaseDao.getEntitiesByField(ProductImage.class, "productId", id);
        return productImages;
    }

    /**
     * 更新商品图片
     *
     * @param imageUrl
     * @param id
     */
    @Override
    public void updateProductImage(List<String> imageUrl, Long id) {
        double sequence = 0L;
        List<ProductImage> imageArrayList = new ArrayList<ProductImage>();
        if (imageUrl != null && imageUrl.size() > 0) {
            for (int i = 0; i < imageUrl.size(); i++) {
                sequence++;
                ProductImage productImage = new ProductImage();
                productImage.setUrl(imageUrl.get(i).trim());
                productImage.setSequence(sequence);
                productImage.setProductId(id);
                imageArrayList.add(productImage);
            }
        }
        //删除原有的商品图片
        List<ProductImage> oldProductImages = null;
        if (id != null) {
            oldProductImages = jpaBaseDao.getEntitiesByField(ProductImage.class, "productId", id);
        }

        if (oldProductImages != null) {
            for (ProductImage productImage : oldProductImages) {
                jpaBaseDao.removeEntity(productImage);
            }
        }
        //保存新的商品图片
        for (int i = imageArrayList.size() - 1; i >= 0; i--) {
            jpaBaseDao.persist(imageArrayList.get(i));
        }
    }

    /**
     * 更新商品图片
     *
     * @param productImages
     * @param id
     */
    @Override
    public void updateImage(List<ProductImage> productImages, Long id) {
        //删除原有的商品图片
        List<ProductImage> oldProductImages = null;
        if (id != null) {
            oldProductImages = jpaBaseDao.getEntitiesByField(ProductImage.class, "product.id", id);
        }

        if (oldProductImages != null) {
            for (ProductImage productImage : oldProductImages) {
                if (productImage != null) {
                    jpaBaseDao.removeEntity(productImage);
                }
            }
        }
        //保存新的商品图片
        for (ProductImage productImage : productImages) {
            if (productImage != null) {
                jpaBaseDao.persist(productImage);
            }
        }
    }

    @Override
    public List<ProductImage> getProductImageBySequence(Long productId) {
        return productImageDao.getProductImageBySequence(productId);
    }

}
