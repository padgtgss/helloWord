/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.biz;

import com.google.common.base.Preconditions;
import com.pemass.common.core.pojo.Body;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.ArithmeticUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.biz.ProductCategory;
import com.pemass.persist.domain.jpa.biz.ProductImage;
import com.pemass.persist.domain.jpa.biz.Site;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.enumeration.ConfigEnum;
import com.pemass.pojo.biz.ProductImagePojo;
import com.pemass.pojo.biz.ProductPojo;
import com.pemass.pojo.sys.BodyPojo;
import com.pemass.service.pemass.biz.ProductCategoryService;
import com.pemass.service.pemass.biz.ProductImageService;
import com.pemass.service.pemass.biz.ProductService;
import com.pemass.service.pemass.biz.SiteService;
import com.pemass.service.pemass.sys.ConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description: 商品Controller
 * @Author: estn.zuo
 * @CreateTime: 2014-12-10 10:11
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private SiteService siteService;

    @Resource
    private ProductImageService productImageService;

    @Resource
    private ProductCategoryService productCategoryService;

    @Resource
    private ConfigService configService;

    /**
     * 根据ID来获取营业点所属组织下 上架状态的商品列表
     *
     * @param siteId
     * @param pageIndex
     * @param pageSize
     * @return
     */

    @RequestMapping(value = "/search", method = RequestMethod.GET, params = "siteId")
    @ResponseBody
    public Object getProuducts(Long siteId, Long pageIndex, Long pageSize) {

        DomainPage domainPage = new DomainPage();

        if (siteId != null) {
            //获取营业点所属组织
            Site site = siteService.getSiteInfo(siteId);

            Map<String, Object> map = new HashMap<String, Object>();//精确条件
            map.put("productStatus", 2);
            map.put("organizationId", site.getOrganizationId());

            Map<String, Object> fuzzyMap = new HashMap<String, Object>();//模糊条件

            Map<String, Object> compound = new HashMap<String, Object>();//其他条件

            domainPage.setPageIndex(pageIndex);
            domainPage.setPageSize(pageSize);
            domainPage = productService.getAllproducts(map, fuzzyMap, compound, domainPage.getPageIndex(), domainPage.getPageSize());

            List<ProductPojo> productPojoList = new ArrayList<ProductPojo>();
            List<Object[]> productList = domainPage.getDomains();
            for (int i = 0; i < productList.size(); i++) {

                ProductPojo productPojo = new ProductPojo();
                MergeUtil.merge(productList.get(i)[0], productPojo, new String[]{"productDetail", "isReturn", "isInvoice", "isTimePeriod", "expiryPeriod", "expiryPointTime", "onePrice", "loanPrice"});
                productPojoList.add(productPojo);
            }

            domainPage.setDomains(productPojoList);
        }
        return domainPage;

    }


    /**
     * 根据筛选条件搜索商品
     * <p/>
     *
     * @param productName       商品名称( *模糊匹配* )
     * @param productType       商品类型【1-普通商品包含返现商品，2-一元购商品，3-返现】
     * @param productCategoryId 商品分类
     * @param cityId            城市ID
     * @param districtId        地区ID
     * @param orderByFiledName  排序字段
     * @param orderBy           排序类型
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Object getProuducts(String productName, Integer productType, Long productCategoryId, Long cityId, Long districtId,
                               String orderByFiledName, BaseDao.OrderBy orderBy, Long pageIndex, Long pageSize) {

        DomainPage domainPage = new DomainPage();

        List<ProductCategory> secProductCategoryList = new ArrayList<ProductCategory>();
        if (productCategoryId != null) {
            secProductCategoryList = productCategoryService.getSecondLevelCategory(productCategoryId);//获取二级分类
        }

        //根据商品名称模糊匹配商品列表
        Map<String, Object> map = new HashMap<String, Object>();//精确条件
        /**1-普通商品包含返现商品，2-一元购商品，3-返现**/
        Preconditions.checkNotNull(productType);
        if (productType.equals(1) || productType.equals(3)) {
            map.put("productStatus", 2);
            if (productType.equals(3)) {
                map.put("isCashback", 1);
            }
        } else {
            map.put("oneProductStatus", 2);
        }

        if (cityId != null) {
            if (StringUtils.isNotBlank(cityId.toString())) {
                map.put("cityId", cityId);
            }
        }

        if (districtId != null) {
            if (StringUtils.isNotBlank(districtId.toString())) {
                map.put("districtId", districtId);
            }
        }


        Map<String, Object> fuzzyMap = new HashMap<String, Object>();//模糊条件
        if (StringUtils.isNotBlank(productName)) {
            fuzzyMap.put("productName", productName);
        }

        Map<String, Object> compound = new HashMap<String, Object>();//其他条件

        List<Long> productCategoryIds = new ArrayList<Long>();
        if (secProductCategoryList.size() > 0) {
            for (int i = 0; i < secProductCategoryList.size(); i++) {
                productCategoryIds.add(secProductCategoryList.get(i).getId());
            }
            compound.put("productCategoryIds", productCategoryIds);
        } else {
            if (productCategoryId != null){
                productCategoryIds.add(productCategoryId);
                compound.put("productCategoryIds", productCategoryIds);
            }
        }

        domainPage.setPageIndex(pageIndex);
        domainPage.setPageSize(pageSize);
        domainPage = productService.getAllproducts(map, fuzzyMap, compound, orderByFiledName, orderBy.toString(), domainPage.getPageIndex(), domainPage.getPageSize());
        List<ProductPojo> productPojoList = new ArrayList<ProductPojo>();
        List<Product> productList = domainPage.getDomains();
        for (int i = 0; i < productList.size(); i++) {

            Product product = productList.get(i);

            ProductPojo productPojo = this.fetchPojoFromProduct(product);
            productPojoList.add(productPojo);
        }

        domainPage.setDomains(productPojoList);

        return domainPage;

    }


    /**
     * 根据商品ID获取详情
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public Object getProuductById(@PathVariable("productId") Long productId) {


        Product product = productService.getProductInfo(productId);
        ProductPojo productPojo = this.fetchPojoFromProduct(product);

        /**转换详情内容*/
        List<BodyPojo> bodyPojos = new ArrayList<BodyPojo>();
        for (Body body : product.getProductDetail()) {
            BodyPojo bodyPojo = new BodyPojo();
            MergeUtil.merge(body, bodyPojo);
            bodyPojos.add(bodyPojo);
        }
        productPojo.setProductDetail(bodyPojos);

        /**获取商品图片信息*/
        List<ProductImage> productImageList = productImageService.getProductImageList(productId);
        List<ProductImagePojo> productImagePojoList = new ArrayList<ProductImagePojo>();
        for (int i = 0; i < productImageList.size(); i++) {
            ProductImagePojo productImagePojo = new ProductImagePojo();
            MergeUtil.merge(productImageList.get(i), productImagePojo);
            productImagePojoList.add(productImagePojo);
        }
        productPojo.setProductImagePojoList(productImagePojoList);


        /**
         * 获取原始商品的库存
         */
        Product originProduct = productService.getProductInfo(product.getOriginProductId());
        productPojo.setStockNumber(originProduct.getStockNumber());

        return productPojo;

    }

    private ProductPojo fetchPojoFromProduct(Product product) {
        ProductPojo productPojo = new ProductPojo();
        MergeUtil.merge(product, productPojo);

        /**赠送一元购积分数**/
        Config config = configService.getConfigByKey(ConfigEnum.POINT_O_GIVING_RATIO);
        Float rate = Float.valueOf(config.getValue());
        productPojo.setGivingOnumber(ArithmeticUtil.ceil(product.getOutPrice() * rate));
        /**一元购手续费**/
        config = configService.getConfigByKey(ConfigEnum.LOAN_RATE);
        rate = Float.valueOf(config.getValue());
        Double o = Math.ceil((product.getOutPrice() - 1) * rate);
        productPojo.setPoundage(o.doubleValue());

        return productPojo;
    }

}
