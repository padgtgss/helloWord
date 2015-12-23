package com.pemass.service.pemass.biz;

import com.pemass.common.core.pojo.Body;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.biz.ProductImage;
import com.pemass.persist.domain.jpa.sys.Organization;

import java.util.List;
import java.util.Map;

/**
 * @author luoc
 * @Description: ${todo}
 * @Description: ${todo}
 * @date 2014/10/13
 */
public interface ProductService {

    /**
     * 保存商品
     *
     * @param product
     */
    void saveProduct(Product product);

    /**
     * 根据ID查询商品
     *
     * @param productId
     * @return
     */
    Product selectProductById(Long productId);

    /**
     * 更新商品
     *
     * @param source
     * @return
     */
    Product updateProduct(Product source);

    /**
     * 多条件查询商品
     *
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param compoundFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getAllproducts(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize);


    /**
     * 多条件查询商品
     *
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param compoundFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getAllproducts(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap,
                              Map<String, Object> compoundFieldNameValueMap, String fieldName,
                              String sortIn, long pageIndex, long pageSize);

    /**
     * 删除商品
     *
     * @param id
     */
    void deleteProduct(Long id);

    /**
     * 下架商品
     *
     * @param id
     * @return
     */
    void updatedDisable(Long id);

    /**
     * 验证商品是否能上架
     *
     * @param ids
     * @return
     */
    Boolean isShelves(String ids);

    /**
     * 上架商品
     *
     * @param id
     * @return
     */
    Boolean updateShelves(Long id);

    /**
     * 获取商品信息
     *
     * @param id
     * @return
     */
    Product getProductInfo(Long id);

    /**
     * 修改标准商品（样板商品 - 审核后台）
     *
     * @param product
     * @return
     */
    Product updateStandardCommodity(Product product);

    /**
     * 查询所有已上架商品集合
     *
     * @param map
     * @return
     */
    List<Product> getProductList(Map<String, Object> map);

    /**
     * 商户新增商品
     *
     * @param product
     * @return
     */
    Product addProduct(Product product, String isOneProduct);

    /**
     * 查询上级可以分销的商品
     *
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getParentProductList(Long orgId, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 查询当前商品的分销情况
     *
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getProductDistributionDetail(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * body排序
     *
     * @param title
     * @param imageUrl
     * @param content
     * @return
     */
    List<Body> getProductDetailBodies(List<String> title, List<String> imageUrl, List<String> content);

    /**
     * 封装Body(审核后台 - 修改商品信息<景区标准门票>)
     *
     * @param bodies
     * @return
     */
    String getBodies(List<Body> bodies);


    /**
     * 对可分销的商品操作：加入商品仓库/直接上架
     *
     * @param product
     * @return
     */
    Product addProductDepot(Product product, Organization organization, Integer productStatus, List<ProductImage> productImageList);

    /**
     * 查询分销商分销的某供应商的商品
     *
     * @param parentAccountId
     * @param accountId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getDistributionProduct(Long parentAccountId, Long accountId, Long pageIndex, Long pageSize);

    /**
     * 查询所有商品
     *
     * @param map
     * @return
     */
    List<Product> getAllProducts(Map<String, Object> map);

    /**
     * 获取满足条件的商品
     *
     * @param conditions
     * @param fuzzyConditions
     * @param domainPage
     * @return
     */
    DomainPage getProductsByConditions(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, Map<String, List<Object>> collectionConditions, DomainPage domainPage);

    /**
     * 获取满足条件点商品
     *
     * @param conditions
     * @param domainPage
     * @return
     */
    DomainPage getProductsByConditions(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 新增一个商品图片
     *
     * @param productImage
     */
    void insertImage(ProductImage productImage);

    /**
     * 商品的库存是否满足需要
     *
     * @param productId
     * @param needAmount
     * @return
     */
    boolean checkProductUsableAmount(Long productId, Integer needAmount);

    /**
     * 修改非标准票
     *
     * @param product
     */
    void updateNonStandardProduct(Product product, String isOneProduct);

    /**
     * 把带有百分号的字符串转换为浮点型
     *
     * @param str
     * @return
     */
    float turnStringtoFloat(String str);


    /**
     * 四舍五入
     *
     * @param str
     * @param value 保留位数
     * @return
     */
    Float rounded(String str, int value);

    /**
     * 拆分id字符串，并根据状态执行service
     *
     * @param ids  id字符串
     * @param flag 执行参数  shelves 上架 disable 下架 delete 删除
     */
    void updateProductStatus(String ids, String flag);

    /**
     * 查询分销的商品列表
     *
     * @param parentAccountId
     * @param accountId
     * @return
     */
    List getDistributionProduct(Long parentAccountId, Long accountId);

    /**
     * 查询一元购商品
     * 重定向URL参数拼接
     *
     * @param url url地址
     * @param map 参数集合
     * @return
     */
    String urlAssem(String url, Map<String, Object> map);

    /**
     * 计算积分受理费
     *
     * @param organization
     * @param product
     * @return
     */
    float computePointAmount(Organization organization, Product product);

    /**
     * 计算返现金额
     *
     * @param product
     * @return
     */
    float computeCashbackAmount(Product product);

    /**
     * 计算一元购贷款利息
     *
     * @param outPrice
     * @param loan
     * @return
     */
    double computeUserLoan(double outPrice, float loan);

    /**
     * 计算一元购赠送积分
     *
     * @param outPrice
     * @return
     */
    double givePoint(double outPrice);


    /**
     * 更新商品销售量
     *
     * @param purchaseProduct
     * @param amount
     */
    void updateProductSaleNumber(Product purchaseProduct, Integer amount);

    /**
     * 更新商品库存
     *
     * @param purchaseProduct
     * @param amount
     */
    void updateProductStockNumber(Product purchaseProduct, Integer amount);

    /**
     * 获取满足条件的商品
     *
     * @param conditions 条件
     * @return 返回值
     */
    DomainPage<Map<String, Object>> getOneProductsByPage(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 验证是否是合法数据
     * @param product
     * @return
     */
    boolean isLegalData(Product product);

    /**
     *
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getHasDistributionProductList(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap,Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 统计所有商品的数量
     * @return
     */
    Long getAllProductAmount();

    /**
     * 统计所有上架商品的数量
     * @return
     */
    Long getShelvesProductAmount();







}
