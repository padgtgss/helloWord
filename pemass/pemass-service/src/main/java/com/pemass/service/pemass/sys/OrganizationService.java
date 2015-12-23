/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;


import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.sys.Company;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.AccountRoleEnum;

import java.util.List;
import java.util.Map;

/**
 * @Description: AccountConfigService
 * @Author: zh
 * @CreateTime: 2014-10-13 13:20
 */
public interface OrganizationService {

    /**
     * 根据id查询商户信息
     *
     * @param id
     * @return
     */
    Organization getOrganizationById(Long id);

    /**
     * 更新商户信息
     *
     * @param source
     * @return
     */
    Organization updateOrganization(Organization source);

    /**
     * 跟新商户信息的同时
     * 建立商户与积分的关系
     *
     * @param source
     * @param pointPoolIdsStr
     * @return
     */
    Organization updateAfterCreateConnection(Organization source, String pointPoolIdsStr);

    /**
     * 分业获取满足条件的商户信息
     *
     * @param conditions
     * @param fuzzyConditions
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getOrganizationsByConditions(Map<String, Object> conditions, Map<String, Object> fuzzyConditions,
                                            Map<String, List<Object>> collectionConditions, long pageIndex, long pageSize);


    /**
     * 分业获取满足条件的商户信息
     *
     * @param condition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage<Map<String, Object>> getPagesByConditions(Map<String, Object> condition, long pageIndex, long pageSize);

    /**
     * 获取受理一元购积分商户
     *
     * @param conditions
     * @param domainPage
     * @return
     */
    DomainPage<Map<String, Object>> getOnePointAccredit(Map<String, Object> conditions, DomainPage<Organization> domainPage);

    Organization updateOrganization(String str[], Organization organization);

    /**
     * 根据Organization的id字符串获取对应的Organization集合
     *
     * @param ids
     * @return
     */
    List<Organization> getOrganizationByIds(String ids);

    /**
     * 获取满足条件的商户（运营审核后台新增站内信）
     * （manager根据所在company管理的省给省内的）商户推送消息
     *
     * @return
     */
    Object getOrganizationByCondition(String pushType, Company company, String accountRoles, String cityIds, String organizationManagerNames);

    /**
     * 获取满足条件的商户
     *
     * @param conditions
     * @return
     */
    DomainPage<Organization> getOrganizationByCondition(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 新增商户
     *
     * @param organization
     */
    Organization insertOrganization(Organization organization);

    /**
     * 查询所有商家
     *
     * @return
     */
    List<Organization> getLists();

    /**
     * 判断商户名称是否重复
     *
     * @param organizationName
     * @return
     */
    boolean hasBeing(String organizationName);


    /**
     * 根据对象的属性名和值完全匹配查询一个实体
     *
     * @param fieldName  属性名
     * @param fieldValue 值
     * @return
     */
    Organization getEntityByField(String fieldName, Object fieldValue);

    /**
     * 审核后台  根据省 查询所在的商家
     *
     * @param provinceId 省id
     * @return
     */
    List<Organization> getOrganizationByProvinceId(Long provinceId);

    /**
     * 审核后台  根据管理区域 查询所有商家
     *
     * @param id
     * @return
     */
    List<Organization> getOrganizationByArea(Long id);

    /**
     * 查询不包含当前id的商户
     *
     * @param terminalUserId
     * @param provinceId
     * @return
     */
    List<Organization> getOrganizationByNoIds(Long terminalUserId, Long provinceId);

    /**
     * 根据条件获取商户
     *
     * @param merchantALowerLevel
     * @param fuzzy
     * @param id
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getOrganizationInfoByDisCode(Map<String, Object> merchantALowerLevel, Map<String, Object> fuzzy, Long id, long pageIndex, long pageSize);

    /**
     * 获取省下面的商户
     *
     * @param provinceId
     * @return
     */
    List<Organization> getOrganizationByProvince(Long provinceId);

    /**
     * 查询出不是特约商户的商户
     *
     * @param fieldMap
     * @return
     */
    DomainPage selectIsNotParticularOrganization(Map<String, Object> fieldMap, long pageIndex, long pageSize);


    /**
     * 获取认购了某个类型的积分的商户
     *
     * @return
     */
    DomainPage selectWithPointPool(Long pointPoolId, long pageIndex, long pageSize);

    /**
     * 根据条件获取商户列表
     *
     * @return
     */
    List<Organization> getOrganizationLists(Map<String, Object> fieldMap);

    /**
     * 根据角色获取该角色商户的ID集合
     *
     * @param accountRole 角色
     * @return 返回结果
     */
    List<Long> getOrganizationIDByAccountRole(AccountRoleEnum accountRole);

    /**
     * 商户总数量
     * @return
     */
    Long getAllOrganizationAmount();

}