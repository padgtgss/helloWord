/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.domain.jpa.biz.Product;

import java.util.List;
import java.util.Map;

/**
 * @Description: ProductDao
 * @Author: cassiel.liu
 * @CreateTime: 2014-11-05 17:47
 */

public interface ProductDao extends BaseDao {
    /**
     * 获取可以分销的商品
     *
     * @param clazz
     * @param orgId
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     */
    <T extends BaseDomain> DomainPage getEntitiesPagesByFieldList(Class<T> clazz, Long orgId,
                                                                  Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 多条件查询
     *
     * @param clazz
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param compoundFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     */
    <T extends BaseDomain> DomainPage getProductsByCompoundConditions(Class<T> clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap,
                                                                      Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize);


    /**
     * 多条件查询
     *
     * @param clazz
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param compoundFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     */
    <T extends BaseDomain> DomainPage getProductsByCompoundConditions(Class clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap,
                                                                      Map<String, Object> compoundFieldNameValueMap,String fieldName,String sortIn, long pageIndex, long pageSize);


    /**
     * 查询商户分销的商品
     *
     * @param clazz
     * @param parentAccountId
     * @param accountId
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     */
    <T extends BaseDomain> DomainPage getDistributionProduct(Class clazz, Long parentAccountId, Long accountId, long pageIndex, long pageSize);

    List<Product> getShelvesOfProducts(Map<String, Object> map);

    List<Product> getRelatedProducts(Long productId);

    List getDistributionProduct(Long parentAccountId, Long accountId);

    /**
     * 查询商品分销详情
     *
     * @param clazz
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getProductDistributionDetail(Class clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 获取满足条件的虚拟商品
     *
     * @param domainPage
     * @return
     */
    DomainPage getVirtualProductByConditions(Map<String, Object> conditions, DomainPage domainPage);

    <T extends BaseDomain> void updateProductSaleNumber(Class<T> clazz, List<String> params, Integer amount);


    /**
     * 分销商查询已分销的商品
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param compoundFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage<Product> getHasDistributionProductList(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize);

    void updateProductStockNumber(Long id, Integer amount);

    /**
     * 统计所有上架商品的数量
     * @return
     */
    Long getShelvesProductAmount();
}
