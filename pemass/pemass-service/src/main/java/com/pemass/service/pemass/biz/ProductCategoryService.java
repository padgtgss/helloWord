/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.biz.ProductCategory;

import java.util.List;
import java.util.Map;

/**
 * @Description: ProductCategoryService
 * @Author: vahoa.ma
 * @CreateTime: 2015-04-23 16:17
 */
public interface ProductCategoryService {

    /**
     * 保存商品分类
     *
     * @param productCategory
     */
    void saveProductCategory(ProductCategory productCategory);

    /**
     * 更新商品分类
     *
     * @param source
     * @return
     */
    ProductCategory updateProductCategory(ProductCategory source);

    /**
     * 根据ID查询商品分类
     *
     * @param productCategoryId
     * @return
     */
    ProductCategory selectProductCategoryById(Long productCategoryId);

    /**
     * 根据ID删除商品
     *
     * @param productCategoryId
     * @return
     */
    ProductCategory deleteProductCategoryById(Long productCategoryId);

    /**
     * 二级商品类型查询
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage querySecondType(Map<String,Object> fieldNameValueMap,long pageIndex, long pageSize);

    ProductCategory insert(ProductCategory productCategory);

    ProductCategory selectById(Long id);


    ProductCategory update(ProductCategory productCategory);


    /**
     * 获取一级商品类别
     * @return
     */
    List<ProductCategory> getPrimaryCategories();

    /**
     * 查询所有二级类别
     * @return
     */
    List<ProductCategory> getSecondLevelCategory();

    /**
     * 根据一级类别查询二级类别
     * @param id
     * @return
     */
    List<ProductCategory> getSecondLevelCategory(Long id);


    /**
     * 通过id得到商品类型
     * @param id
     * @return
     */
    ProductCategory getProductCategoryById(Long id);

    /**
     * 判断是否是虚拟商品
     * @param productCategory
     * @return
     */
    Map<String,Object> isCreateTicket(ProductCategory productCategory);

}
