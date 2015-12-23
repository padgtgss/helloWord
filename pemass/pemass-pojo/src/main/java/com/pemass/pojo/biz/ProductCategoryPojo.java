package com.pemass.pojo.biz;

import java.util.List;

/**
 * @Description: ProductController
 * @Author: huang zhuo
 * @CreateTime: 2014-11-10 11:40
 */

public class ProductCategoryPojo {
    private Long id;

    private String categoryName;//商品类别

    private List<ProductCategoryPojo> productCategoryPojoList;//二级分类


   //======================== getter and setter ====================================


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ProductCategoryPojo> getProductCategoryPojoList() {
        return productCategoryPojoList;
    }

    public void setProductCategoryPojoList(List<ProductCategoryPojo> productCategoryPojoList) {
        this.productCategoryPojoList = productCategoryPojoList;
    }
}
