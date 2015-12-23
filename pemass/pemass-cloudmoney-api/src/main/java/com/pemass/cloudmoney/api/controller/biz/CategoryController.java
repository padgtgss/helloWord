/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.biz;


import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.biz.ProductCategory;
import com.pemass.pojo.biz.ProductCategoryPojo;
import com.pemass.service.pemass.biz.ProductCategoryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: OrderController
 * @Author: pokl.huang
 * @CreateTime: 2014-11-18 09:40
 */
@Controller
@RequestMapping("/productCategory")
public class CategoryController {

    private Log logger = LogFactory.getLog(CategoryController.class);

    @Resource
    private ProductCategoryService productCategoryService;



    @RequestMapping(value = "/search",method = RequestMethod.GET)
    @ResponseBody
    public Object getCategoryList() {
        List<ProductCategory> productCategoryList = productCategoryService.getSecondLevelCategory(0L);
        List<ProductCategoryPojo> productCategoryPojoList = new ArrayList<ProductCategoryPojo>();
        for(ProductCategory productCategory: productCategoryList) {
            ProductCategoryPojo productCategoryPojo = new ProductCategoryPojo();
            MergeUtil.merge(productCategory,productCategoryPojo);

            List<ProductCategory> secProductCategoryList = productCategoryService.getSecondLevelCategory(productCategory.getId());//获取二级分类
            List<ProductCategoryPojo> secProductCategoryPojoList = new ArrayList<ProductCategoryPojo>();
            for (ProductCategory secProductCategory : secProductCategoryList){

                ProductCategoryPojo secProductCategoryPojo = new ProductCategoryPojo();
                MergeUtil.merge(secProductCategory, secProductCategoryPojo);

                secProductCategoryPojoList.add(secProductCategoryPojo);
            }

            productCategoryPojo.setProductCategoryPojoList(secProductCategoryPojoList);

            productCategoryPojoList.add(productCategoryPojo);
        }


        return productCategoryPojoList;

    }



}

