package com.pemass.service.pemass.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.biz.AccountRelation;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.AuditStatusEnum;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/10/13.
 */
public interface AccountRelationService {

    /**
     * 分页查询关系表
     *
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getAccountRelationList(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 解除分销关系
     *
     * @param accountId
     * @param parentAccountId
     * @return
     */
    void updateRelieveRelation(Long accountId, Long parentAccountId);

    /**
     * 撤销申请
     *
     * @param id
     * @return
     */
    AccountRelation updateRepealApplication(Long id);

    /**
     * 业务关系建立
     *
     * @param accountId
     * @param parentAccountId
     * @return
     */
    AccountRelation insertApplyFor(Long accountId, Long parentAccountId, Product product);

    /**
     * 驳回申请
     *
     * @param accountRelation
     * @return
     */
    AccountRelation updateOverrule(AccountRelation accountRelation);

    /**
     * 通过审核
     *
     * @param accountRelation
     * @return
     */
    AccountRelation updatePass(AccountRelation accountRelation);

    /**
     * 再次申请
     *
     * @param id
     * @return
     */
    AccountRelation updateApplyForAgain(Long id);



    /**
     * 多条件查询关系表集合
     *
     * @param map
     * @return
     */
    List getAllAssociated(Map<String, Object> map);

    /**
     * 未处理申请条数
     *
     * @param organizationId
     * @return
     */
    Long getUntreatedCounts(Long organizationId);

    /**
     * 我的分销商个数
     *
     * @param organizationId
     * @return
     */
    Long getDistributorsCounts(Long organizationId);

    /**
     * 供应商个数
     *
     * @param organizationId
     * @return
     */
    Long getNumberOfSuppliers(Long organizationId);

    /**
     * 我的申请条数
     *
     * @param organizationId
     * @return
     */
    Long getNumberOfApplyFor(Long organizationId);

    /**
     * 根据供应商和分销商Id查询一个关系对象
     *
     * @param accountId
     * @param parentAccountId
     * @return
     */
    AccountRelation getAccountRelation(Long accountId, Long parentAccountId);

    /**
     * 发出邀请
     *
     * @param accountId
     * @param parentAccountId
     * @return
     */
    Boolean sendInvitations(Long accountId, Long parentAccountId);

    /**
     * 发送站内消息
     * @param map
     * @param passwords
     * @return
     */
    Boolean sendInvitation(Map<String, Object> map, String passwords);
    /**
     * 修改业务关系状态
     *
     * @param id
     * @param auditStatu
     * @return
     */
    AccountRelation updateAccountRelationState(Long id, AuditStatusEnum auditStatu);


    /**
     * 查询所有商户
     *
     * @param name      条件查询商户名称
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getAllBusinesses(String name, int accountRole, Long organizationId, long pageIndex, long pageSize);

    /**
     * 查询所有景区
     *
     * @return
     */
    List<Organization> getOrganizationList(Long id);

    /**
     * 根据二级分销商id查询所属分销商对象信息
     *
     * @param accountId
     * @return
     */
    AccountRelation getSecondAccountRelation(Long accountId);

    Long getSecondBusinessCount(Long accountId);

    Long getSiteCount(Organization organization);

    Long getCashierCount(Long accountId);

    Long getAccountManageCount(Organization organization);

    /**/
    public boolean hasBeing(String username);

    /**
     * 查询我的二级分销商信息
     *
     * @return
     */
    DomainPage getSeccondAccountRelationList(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 商品市场查询所有商品
     *
     * @param organizationId            当前公司id
     * @param fieldNameValueMap         精确查询参数
     * @param fuzzyFieldNameValueMap    模糊查询参数
     * @param compoundFieldNameValueMap 范围查询参数
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getAllProduct(long organizationId, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 商品市场载入来源商户
     *
     * @param organizationId
     * @return
     */
    List<Organization> getAllMerchants(long organizationId);

    /**
     * 渠道商查询自己的商户
     *
     * @param fieldNameValueMap      精确匹配条件
     * @param fuzzyFieldNameValueMap 模糊匹配条件
     * @param organizationId         渠道商id
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getChannelMerchants(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long organizationId, long pageIndex, long pageSize);

    /**
     * 渠道商解除和下级商户之间的关系
     *
     * @param organizationId 下级商户的id
     * @return
     */
    boolean removeRelation(long organizationId);

    /**
     * 统计下级商户个数
     *
     * @param organization 渠道商
     * @return
     */
    Long getChannelMerchantsCount(Organization organization);

    /**
     * 申请调价
     *
     * @param accountRelationId 关系表id
     * @param hopeDiscount      期望折扣
     */
    void updateDiscount(long accountRelationId, String hopeDiscount);

    /**
     * 撤销调价申请
     *
     * @param accountRelationId
     */
    void repealApplyForPricing(long accountRelationId);

    /**
     * 查询待处理调价申请
     *
     * @param map  模糊查询条件集合
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getPricingRequestList(Map<String, Object> map, long pageIndex, long pageSize);

    /**
     * 驳回调价申请
     * @param accountRelationId
     */
    void cancel(long accountRelationId);

    /**
     * 通过调价申请
     * @param accountRelationId
     */
    void through(long accountRelationId, String discount);

    /**
     * 查询关系详情
     * @param accountRelationId
     * @return
     */
    AccountRelation getAccountRelationInfo(long accountRelationId);

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
