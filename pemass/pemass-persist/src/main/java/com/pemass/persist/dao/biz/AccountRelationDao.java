package com.pemass.persist.dao.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.sys.Organization;

import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2014/12/17.
 */
public interface AccountRelationDao  extends BaseDao{

    /**
     * 查询所有机构
     * @param name     条件查询机构名称
     * @accountRole    机构类型 0-景区 1-供应商 2-商户
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getAllBusinesses(String name, int accountRole, Long organizationId, long pageIndex, long pageSize);

    /**
     * 商品市场查询所有商品
     * @param organizationId 当前机构id
     * @param fieldNameValueMap 精确查询条件集合
     * @param fuzzyFieldNameValueMap 模糊查询条件集合
     * @param compoundFieldNameValueMap 范围查询条件集合
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getAllProduct(long organizationId, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap,
                             Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 商品市场载入来源商户
     * @param organizationId
     * @return
     */
    List<Organization> getAllMerchants(long organizationId);

    /**
     * 查询待处理调价申请
     */
    DomainPage getPricingRequestList(long organizationId, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);

    DomainPage getAccountRelation(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap,
                                  Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 获取待处理调价申请
     */
    DomainPage getPricingRequestList(Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);

    DomainPage getEntitiesByFieldList(Class clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 查询分销商分销的所有供应商的商品
     * @param organizationId
     * @param parentOrganizationId
     * @return
     */
    List<Product> selectDistributionOfGoods(long organizationId, long parentOrganizationId);

    /**
     * 查询我的分销商
     * @param map
     * @param organizationId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectDistributorByPage(Map<String, Object> map, long organizationId, int auditStatus, int discountStatus, long pageIndex, long pageSize);

    /**
     * 查询我的供应商
     * @param map
     * @param organizationId
     * @param auditStatus
     * @param discountStatus
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectSupplierByPage(Map<String, Object> map, long organizationId, int auditStatus, int discountStatus, long pageIndex, long pageSize);
}
