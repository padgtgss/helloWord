package com.pemass.service.pemass.biz.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.dao.biz.AccountRelationDao;
import com.pemass.persist.dao.biz.ProductDao;
import com.pemass.persist.dao.sys.AccountDao;
import com.pemass.persist.domain.jpa.bas.City;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.persist.domain.jpa.biz.AccountRelation;
import com.pemass.persist.domain.jpa.biz.Notification;
import com.pemass.persist.domain.jpa.biz.NotificationOrganization;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.biz.Site;
import com.pemass.persist.domain.jpa.sys.Account;
import com.pemass.persist.domain.jpa.sys.CityServiceProvider;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.enumeration.AccountRoleEnum;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PositionRoleEnum;
import com.pemass.service.pemass.bas.ProvinceService;
import com.pemass.service.pemass.biz.AccountRelationService;
import com.pemass.service.pemass.sys.AccountService;
import com.pemass.service.pemass.sys.CityServiceProviderService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/10/13.
 */

@Service
public class AccountRelationServiceImpl implements AccountRelationService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private ProductDao productDao;

    @Resource
    private AccountRelationDao accountRelationDao;

    @Resource
    private AccountDao accountDao;

    @Resource
    private AccountService accountService;


    @Resource
    private ProvinceService provinceService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private CityServiceProviderService cityServiceProviderService;

    /**
     * 分页查询信息
     *
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional
    @Override
    public DomainPage getAccountRelationList(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap,Map<String,Object> compoundFieldNameValueMap, long pageIndex, long pageSize) {
        DomainPage domainPage = accountRelationDao.getAccountRelation(fieldNameValueMap,fuzzyFieldNameValueMap,compoundFieldNameValueMap,pageIndex,pageSize);
        long count = domainPage.getTotalCount();
        List<Object[]> list = Lists.newArrayList();
        if(domainPage.getDomains() != null && domainPage.getDomains().size() > 0){
            for(int i = 0;i < domainPage.getDomains().size();i++){
                Object [] objects = (Object [])domainPage.getDomains().get(i);
                long accountRelationId = new BigInteger(objects[0].toString()).longValue();
                long provinceId = new BigInteger(objects[1].toString()).longValue();
                long cityId = new BigInteger(objects[2].toString()).longValue();
                Object [] newObjects = new Object[5];
                newObjects[0] = getAccountRelationInfo(accountRelationId);
                newObjects[1] = organizationService.getOrganizationById(((AccountRelation)newObjects[0]).getOrganizationId());
                newObjects[2] = organizationService.getOrganizationById(((AccountRelation)newObjects[0]).getParentOrganizationId());
                newObjects[3] = provinceService.getProvinceByID(provinceId);
                newObjects[4] = provinceService.getCityById(cityId);
                if(null != fuzzyFieldNameValueMap && fuzzyFieldNameValueMap.size() > 0){
                    if(compoundFieldNameValueMap.get("flag").equals("0")){
                        if(((Organization)newObjects[2]).getOrganizationName().indexOf(fuzzyFieldNameValueMap.get("organization_name").toString()) != -1){
                            list.add(newObjects);
                        }else{
                           count--;
                        }
                    }else{
                        if(((Organization)newObjects[1]).getOrganizationName().indexOf(fuzzyFieldNameValueMap.get("organization_name").toString()) != -1){
                            list.add(newObjects);
                        }else{
                            count--;
                        }
                    }
                }else{
                    list.add(newObjects);
                }
            }
        }
        domainPage.setTotalCount(count);
        domainPage.setDomains(list);
        return domainPage;
    }

    /**
     * 解除分销关系
     *
     * @param accountId
     * @param parentAccountId
     * @return
     */
    @Override
    public void updateRelieveRelation(Long accountId, Long parentAccountId) {
        //===================解除分销关系==============
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("organizationId", accountId);
        map.put("parentOrganizationId", parentAccountId);
        DomainPage domainPage = jpaBaseDao.getEntitiesPagesByFieldList(AccountRelation.class, map, 1l, 1l);
        List<AccountRelation> accountRelations = domainPage.getDomains();
        if (accountRelations.size() > 0 || null != accountRelations) {
            AccountRelation accountRelation = accountRelations.get(0);
            accountRelation.setAvailable(AvailableEnum.UNAVAILABLE);
            jpaBaseDao.merge(accountRelations.get(0));
        }
        //==================取消所有分销商品销售权============
        List<Product> list = accountRelationDao.selectDistributionOfGoods(accountId,parentAccountId);
        if(list != null && list.size() > 0){
            for(Product product :list){
                product.setAvailable(AvailableEnum.UNAVAILABLE);
                jpaBaseDao.merge(product);
            }
        }
    }


    /**
     * 撤销申请
     *
     * @param id
     * @return
     */
    @Override
    public AccountRelation updateRepealApplication(Long id) {
        AccountRelation accountRelation = jpaBaseDao.getEntityByField(AccountRelation.class, "id", id);
        accountRelation.setAvailable(AvailableEnum.UNAVAILABLE);
        accountRelation = jpaBaseDao.merge(accountRelation);
        return accountRelation;
    }

    /**
     * 申请成为分销商
     *
     * @param accountId
     * @param parentAccountId
     * @return
     */
    @Override
    public AccountRelation insertApplyFor(Long accountId, Long parentAccountId, Product product) {
        AccountRelation accountRelation = new AccountRelation();
        accountRelation.setOrganizationId(accountId);
        accountRelation.setParentOrganizationId(parentAccountId);
        accountRelation.setApplyTime(new Date());
        accountRelation.setAuditStatus(AuditStatusEnum.ING_AUDIT);
        accountRelation.setDiscountStatus(AuditStatusEnum.NONE_AUDIT);
        if (product != null) {
//            product = productService.getProductInfo(product.getId());
            accountRelation.setProductId(product.getId());
        }

        jpaBaseDao.persist(accountRelation);
        return accountRelation;
    }

    /**
     * 驳回申请
     *
     * @param accountRelation
     * @return
     */
    @Override
    public AccountRelation updateOverrule(AccountRelation accountRelation) {
        AccountRelation relation = jpaBaseDao.getEntityByField(AccountRelation.class, "id", accountRelation.getId());
        relation.setAuditStatus(AuditStatusEnum.FAIL_AUDIT);
        relation.setRemark(accountRelation.getRemark());
        accountRelation = jpaBaseDao.merge(relation);
        return accountRelation;
    }

    /**
     * 通过审核
     *
     * @param accountRelation
     * @return
     */
    @Override
    public AccountRelation updatePass(AccountRelation accountRelation) {
        AccountRelation relation = jpaBaseDao.getEntityByField(AccountRelation.class, "id", accountRelation.getId());
        relation.setRemark(accountRelation.getRemark());

        relation.setPath("/" + relation.getParentOrganizationId() + "/" + relation.getOrganizationId() + "/");
        relation.setAuditStatus(AuditStatusEnum.HAS_AUDIT);
        relation.setDiscountStatus(AuditStatusEnum.NONE_AUDIT);
        accountRelation = jpaBaseDao.merge(relation);
        return accountRelation;
    }

    /**
     * 再次申请
     *
     * @param id
     * @return
     */
    @Override
    public AccountRelation updateApplyForAgain(Long id) {
        AccountRelation accountRelation = jpaBaseDao.getEntityById(AccountRelation.class, id);

        accountRelation.setApplyTime(new Date());
        accountRelation.setAuditStatus(AuditStatusEnum.ING_AUDIT);
        accountRelation = jpaBaseDao.merge(accountRelation);

        return accountRelation;
    }

    /**
     * 多条件查询关系表集合
     *
     * @param map
     * @return
     */
    @Override
    public List getAllAssociated(Map<String, Object> map) {
        List list = jpaBaseDao.getEntitiesByFieldList(AccountRelation.class, map);
        List<Object []> newList = Lists.newArrayList();
        if(list.size() > 0 && list != null){
            for(int i = 0;i < list.size();i++){
                Object [] objects = new Object[2];
                objects[0] = list.get(i);
                objects[1] = organizationService.getOrganizationById(((AccountRelation) list.get(i)).getParentOrganizationId());
               newList.add(objects);
            }
        }
        return newList;
    }

    /**
     * 未处理申请条数
     *
     * @param organizationId
     * @return
     */
    @Override
    public Long getUntreatedCounts(Long organizationId) {
        Map<String, Object> untMap = new HashMap<String, Object>();
        untMap.put("parentOrganizationId", organizationId);
        untMap.put("auditStatus", AuditStatusEnum.ING_AUDIT);
        return jpaBaseDao.getEntitiesPagesByFieldList(AccountRelation.class, untMap, 1L, 1L).getTotalCount();
    }

    /**
     * 我的分销商个数
     *
     * @param organizationId
     * @return
     */
    @Override
    public Long getDistributorsCounts(Long organizationId) {
        Map<String, Object> disMap = new HashMap<String, Object>();
        disMap.put("parentOrganizationId", organizationId);
        disMap.put("auditStatus", AuditStatusEnum.HAS_AUDIT);
        return jpaBaseDao.getEntitiesPagesByFieldList(AccountRelation.class, disMap, 1L, 1L).getTotalCount();
    }

    /**
     * 我的供应商个数
     *
     * @param organizationId
     * @return
     */
    @Override
    public Long getNumberOfSuppliers(Long organizationId) {
        Map<String, Object> disMap = new HashMap<String, Object>();
        disMap.put("organizationId", organizationId);
        disMap.put("auditStatus", AuditStatusEnum.HAS_AUDIT);
        return jpaBaseDao.getEntitiesPagesByFieldList(AccountRelation.class, disMap, 1L, 1L).getTotalCount();
    }

    /**
     * 我的申请条数
     *
     * @param organizationId
     * @return
     */
    @Override
    public Long getNumberOfApplyFor(Long organizationId) {
        Map<String, Object> disMap = new HashMap<String, Object>();
        disMap.put("organizationId", organizationId);
        return jpaBaseDao.getEntitiesPagesByFieldList(AccountRelation.class, disMap, 1L, 1L).getTotalCount();
    }

    /**
     * 根据供应商和分销商Id查询一个关系对象
     *
     * @param accountId
     * @param parentAccountId
     * @return
     */
    @Override
    public AccountRelation getAccountRelation(Long accountId, Long parentAccountId) {
        AccountRelation accountRelation = null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("organizationId", accountId);
        map.put("parentOrganizationId", parentAccountId);
        map.put("auditStatus", AuditStatusEnum.FAIL_AUDIT);
        List<Object []> list = getAllAssociated(map);
        if (list.size() != 0 && null != list) {
            Object [] object =  list.get(0);
            accountRelation = (AccountRelation)object[0];
        }
        return accountRelation;
    }

    /**
     * 发出邀请
     *
     * @param accountId
     * @param parentAccountId
     * @return
     */
    @Transactional
    @Override
    public Boolean sendInvitations(Long accountId, Long parentAccountId) {
        Organization organization = organizationService.getOrganizationById(accountId);
        Organization parentOrganization = organizationService.getOrganizationById(parentAccountId);

        StringBuffer title = new StringBuffer();
        title.append(parentOrganization.getOrganizationName() + "分销邀请函");

        StringBuffer content = new StringBuffer();
        content.append(organization.getOrganizationName() + ":");
        content.append("<br/>");
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好，我们是" + parentOrganization.getOrganizationName() + "，通过PEMASS系统了解到贵公司业务能力优秀且主营业务与我司相关商品贴合。我们非常想邀请您成为我司产品的分销商。");
        content.append("<br/>");
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如果您也有意与我公司合作、共谋发展，你可以通过PEMass系统，在分销商管理中查找“" + parentOrganization.getOrganizationName() + "”并申请成为我们的分销商。");
        content.append("<br/>");
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供销关系一旦确认，我方将以优质的商品及服务，低廉的成本和较高的收益对您进行销售支持。");
        content.append("<br/>");
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我们希望能得到您的支持与关注！");
        NotificationOrganization notificationOrganization = new NotificationOrganization();
        notificationOrganization.setIsRead(false);
        notificationOrganization.setOrganizationId(organization.getId());
        Notification notification = new Notification();
        notification.setIssuer(parentOrganization.getOrganizationName());
        notification.setContent(content.toString());
        notification.setSummary("");
        notification.setIssueTime(new Date());
        notification.setTitle(title.toString());
        jpaBaseDao.persist(notification);
        notificationOrganization.setNotificationId(notification.getId());
        jpaBaseDao.persist(notificationOrganization);
        return true;
    }


    /**
     * 发送站内消息
     * @param map
     * @param passwords
     * @return
     */
    @Transactional
    @Override
    public Boolean sendInvitation(Map<String,Object> map,String passwords) {
        Long id = 0L;//重置密码对象Id
        String name = "";//重置密码对象name
        Organization organization = null;
        if (map.get("name").equals("account")){
            Account account = (Account)map.get("account");
            organization = organizationService.getOrganizationById(account.getOrganizationId());
            name = account.getAccountname();
            id = organization.getId();
        } else {
            TerminalUser terminalUser = (TerminalUser)map.get("terminalUser");
            organization = organizationService.getOrganizationById(terminalUser.getOrganizationId());
            name = terminalUser.getTerminalUsername();
            id = organization.getId();
        }
        StringBuffer title = new StringBuffer();
        title.append(name + ",重置密码");

        StringBuffer content = new StringBuffer();
        content.append(name + ":");
        content.append("<br/>");
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好，您的密码已重置。这是您的新密码：");
        content.append("<br/>");
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + passwords);
        NotificationOrganization notificationOrganization = new NotificationOrganization();
        notificationOrganization.setIsRead(false);
        notificationOrganization.setOrganizationId(id);
        Notification notification = new Notification();
        notification.setIssuer(organization.getOrganizationName());
        notification.setContent(content.toString());
        notification.setSummary("");
        notification.setIssueTime(new Date());
        notification.setTitle(title.toString());
        jpaBaseDao.persist(notification);
        notificationOrganization.setNotificationId(notification.getId());
        jpaBaseDao.persist(notificationOrganization);
        return true;

    }


    /**
     * 再次申请
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public AccountRelation updateAccountRelationState(Long id, AuditStatusEnum auditStatu) {
        AccountRelation accountRelation = jpaBaseDao.getEntityById(AccountRelation.class, id);

        accountRelation.setApplyTime(new Date());
        accountRelation.setAuditStatus(auditStatu);
        accountRelation = jpaBaseDao.merge(accountRelation);

        return accountRelation;
    }

    /**
     * 查询所有商户
     *
     * @param name      条件查询商户名称
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public DomainPage getAllBusinesses(String name, int accountRole, Long organizationId, long pageIndex, long pageSize) {
        DomainPage domainPage = accountRelationDao.getAllBusinesses(name, accountRole, organizationId, pageIndex, pageSize);
        if (domainPage.getDomains().size() > 0 && domainPage != null) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Object[] objects = (Object[]) domainPage.getDomains().get(i);
                BigInteger bigInteger1 = new BigInteger(objects[2].toString());
                BigInteger bigInteger2 = new BigInteger(objects[3].toString());
                long cityId = bigInteger1.longValue();
                long provinceId = bigInteger2.longValue();
                City city = jpaBaseDao.getEntityById(City.class, cityId);
                objects[2] = city;
                Province province = jpaBaseDao.getEntityById(Province.class, provinceId);
                objects[3] = province;
            }
        }
        return domainPage;
    }

    /**
     * 查询所有授权景区
     *
     * @return
     */
    @Override
    public List<Organization> getOrganizationList(Long id) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("organization.id", id);
        map.put("auditStatus", AuditStatusEnum.HAS_AUDIT);

        List<Organization> organizations = jpaBaseDao.getEntitiesByField(Organization.class, "accountRole", AccountRoleEnum.ROLE_LANDSCAPE);
        List<AccountRelation> accountRelations = jpaBaseDao.getEntitiesByFieldList(AccountRelation.class, map);
        List<Organization> organizationList = new ArrayList<Organization>();

        for (Organization organization : organizations) {
            for (AccountRelation accountRelation : accountRelations) {
                if (accountRelation.getParentOrganizationId().equals(organization.getId())) {
                    organizationList.add(organization);
                }
            }
        }
        return organizationList;
    }

    /**
     * 根据二级分销商id查询所属分销商对象信息
     *
     * @param accountId
     * @return
     */
    @Override
    public AccountRelation getSecondAccountRelation(Long accountId) {
        AccountRelation accountRelation = null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("organizationId", accountId);
        map.put("auditStatus", AuditStatusEnum.HAS_AUDIT);
        List<AccountRelation> list = getAllAssociated(map);
        if (list != null && list.size() > 0) {
            accountRelation = (AccountRelation)getAllAssociated(map).get(0);
        }
        return accountRelation;
    }

    @Override
    public Long getSecondBusinessCount(Long accountId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parentOrganizationId", accountId);
        return jpaBaseDao.getEntitiesPagesByFieldList(AccountRelation.class, map, 1L, 1L).getTotalCount();
    }

    @Override
    public Long getSiteCount(Organization organization) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("organizationId", organization.getId());
        return jpaBaseDao.getEntitiesPagesByFieldList(Site.class, map, 1L, 1L).getTotalCount();
    }

    @Override
    public Long getCashierCount(Long accountId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("organizationId", accountId);
        return jpaBaseDao.getEntitiesPagesByFieldList(TerminalUser.class, map, 1L, 1L).getTotalCount();
    }

    @Override
    public Long getAccountManageCount(Organization organization) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("organizationId", organization.getId());
        return accountDao.getAccountManageCount(Account.class, map);
    }

    @Override
    public boolean hasBeing(String username) {
        return accountDao.hasBeing(Account.class, username) != null;
    }

    /**
     * 分页查询信息
     *
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional
    @Override
    public DomainPage getSeccondAccountRelationList(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        DomainPage domainPage = accountRelationDao.getEntitiesByFieldList(AccountRelation.class, fieldNameValueMap, fuzzyFieldNameValueMap, pageIndex, pageSize);
        List list = new ArrayList();
        for (int i = 0; i < domainPage.getDomains().size(); i++) {
            Object[] objects = new Object[3];
            AccountRelation relation = (AccountRelation) domainPage.getDomains().get(i);
            objects[0] = relation;
            Account account = accountService.getAccountByConditions(relation.getOrganizationId(), PositionRoleEnum.POSITION_ROLE_ADMIN);
            objects[1] = account;
            Organization org = organizationService.getOrganizationById(relation.getOrganizationId());
            objects[2] = org;
            list.add(objects);
        }
        domainPage.setDomains(list);
        return domainPage;
    }

    @Override
    public DomainPage getAllProduct(long organizationId, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize) {
        return accountRelationDao.getAllProduct(organizationId, fieldNameValueMap, fuzzyFieldNameValueMap, compoundFieldNameValueMap, pageIndex, pageSize);
    }

    @Override
    public List<Organization> getAllMerchants(long organizationId) {
        return accountRelationDao.getAllMerchants(organizationId);
    }

    @Override
    public DomainPage getChannelMerchants(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long organizationId, long pageIndex, long pageSize) {
        DomainPage domainPage =  accountDao.getChannelMerchants(Organization.class, fieldNameValueMap, fuzzyFieldNameValueMap, organizationId, pageIndex, pageSize);
        List<Object[]> list = Lists.newArrayList();
        if (domainPage.getDomains().size() > 0 && domainPage != null) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Object[] objects = new Object[4];
                Organization org=(Organization)domainPage.getDomains().get(i);
                objects[0]= org;
                Long provinceId = org.getProvinceId();
                Long cityId = org.getCityId();
                Province province = provinceService.getProvinceByID(provinceId);
                objects[1] = province;
                City city = provinceService.getCityById(cityId);
                objects[2] = city;
                CityServiceProvider cityServiceProvider = null;
                if(org.getCityServiceId() != null){
                    cityServiceProvider  = cityServiceProviderService.getById(org.getCityServiceId());
                    objects[3] = cityServiceProvider;
                }else{
                    objects[3] = null;
                }
                list.add(objects);
            }
            domainPage.setDomains(list);
        }
        return domainPage;
    }

    @Transactional
    @Override
    public boolean removeRelation(long organizationId) {
        Organization organization = jpaBaseDao.getEntityById(Organization.class, organizationId);
        jpaBaseDao.merge(organization);
        return false;
    }

    @Override
    public Long getChannelMerchantsCount(Organization organization) {
        Map<String, Object> fieldNameValueMap = Maps.newHashMap();
        fieldNameValueMap.put("channelId", organization.getId());
        Map<String, Object> fuzzyFieldNameValueMap = Maps.newHashMap();
        return getChannelMerchants(fieldNameValueMap, fuzzyFieldNameValueMap, organization.getId(), 1L, 1L).getTotalCount();
    }

    @Override
    public void updateDiscount(long accountRelationId, String hopeDiscount) {
        AccountRelation accountRelation = jpaBaseDao.getEntityById(AccountRelation.class, accountRelationId);
        accountRelation.setHopeDiscount(hopeDiscount);
        accountRelation.setDiscountStatus(AuditStatusEnum.ING_AUDIT);
        jpaBaseDao.merge(accountRelation);
    }

    @Transactional
    @Override
    public void repealApplyForPricing(long accountRelationId) {
        AccountRelation accountRelation = jpaBaseDao.getEntityById(AccountRelation.class, accountRelationId);
        accountRelation.setHopeDiscount(null);
        accountRelation.setDiscountStatus(AuditStatusEnum.NONE_AUDIT);
        jpaBaseDao.merge(accountRelation);
    }

    @Override
    public DomainPage getPricingRequestList(Map<String,Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        DomainPage domainPage = accountRelationDao.getPricingRequestList(fuzzyFieldNameValueMap,pageIndex,pageSize);
        long count = domainPage.getTotalCount();
        List<Object []> list = Lists.newArrayList();
        if(domainPage.getDomains() != null && domainPage.getDomains().size() > 0){
            for(int i = 0;i < domainPage.getDomains().size();i++){
                Object [] objects = (Object[]) domainPage.getDomains().get(i);
                long accountRelationId = new BigInteger(objects[0].toString()).longValue();

                Object [] newObjects = new Object[2];
                newObjects[0] = getAccountRelationInfo(accountRelationId);
                newObjects[1] = organizationService.getOrganizationById(((AccountRelation)newObjects[0]).getOrganizationId());

                if(null != fuzzyFieldNameValueMap && fuzzyFieldNameValueMap.size() > 0){
                  if(organizationService.getOrganizationById(((AccountRelation)newObjects[0]).getOrganizationId()).getOrganizationName().indexOf(fuzzyFieldNameValueMap.get("organization_name").toString()) != -1){
                      list.add(newObjects);
                  }else{
                      count--;
                  }
                }else{
                    list.add(newObjects);
                }
            }
        }
        domainPage.setTotalCount(count);
        domainPage.setDomains(list);
        return domainPage;
    }

    @Transactional
    @Override
    public void cancel(long accountRelationId) {
        AccountRelation accountRelation = jpaBaseDao.getEntityById(AccountRelation.class,accountRelationId);
        accountRelation.setDiscountStatus(AuditStatusEnum.FAIL_AUDIT);
        jpaBaseDao.merge(accountRelation);
    }

    @Transactional
    @Override
    public void through(long accountRelationId,String discount) {
        AccountRelation accountRelation = jpaBaseDao.getEntityById(AccountRelation.class,accountRelationId);
        accountRelation.setDiscountStatus(AuditStatusEnum.HAS_AUDIT);
        accountRelation.setDiscount(discount);
        DomainPage domainPage = getDistributionProduct(accountRelation.getParentOrganizationId(),accountRelation.getOrganizationId(),1L,Long.valueOf(Integer.MAX_VALUE));
       if(domainPage.getDomains() != null && domainPage.getDomains().size() > 0){
           for(int i = 0;i < domainPage.getDomains().size();i++){
               updatePrice((Product)domainPage.getDomains().get(i), discount);
           }
       }
        jpaBaseDao.merge(accountRelation);
    }
    private DomainPage getDistributionProduct(Long parentAccountId, Long accountId, Long pageIndex, Long pageSize) {
        return productDao.getDistributionProduct(Product.class, parentAccountId, accountId, pageIndex, pageSize);
    }
    private void updatePrice(Product product,String discount){
            Product parentProduct = jpaBaseDao.getEntityById(Product.class,product.getParentProductId());
            float value = Float.valueOf(discount);
            double newLowerPrice = parentProduct.getLowerPrice() * (value/10);
            product.setBasePrice(newLowerPrice);
            jpaBaseDao.merge(product);
    }

    @Override
    public AccountRelation getAccountRelationInfo(long accountRelationId) {
        return jpaBaseDao.getEntityById(AccountRelation.class,accountRelationId);
    }

    @Override
    public DomainPage selectDistributorByPage(Map<String, Object> map, long organizationId,int auditStatus,int discountStatus, long pageIndex, long pageSize) {
        DomainPage domainPage = accountRelationDao.selectDistributorByPage(map,organizationId,auditStatus,discountStatus,pageIndex,pageSize);
        List<Object []> list = Lists.newArrayList();
        if(domainPage.getDomains() != null && domainPage.getDomains().size() > 0){
            for(int i = 0;i < domainPage.getDomains().size();i++){
                Object [] objects = (Object[]) domainPage.getDomains().get(i);
                Object [] newObjects = new Object[4];
                long accountRelationId = new BigInteger(objects[0].toString()).longValue();
                long oId = new BigInteger(objects[1].toString()).longValue();
                newObjects[0] = getAccountRelationInfo(accountRelationId);
                newObjects[1] = organizationService.getOrganizationById(oId);
                newObjects[2] = provinceService.getProvinceByID(organizationService.getOrganizationById(oId).getProvinceId());
                newObjects[3] = provinceService.getCityById(organizationService.getOrganizationById(oId).getCityId());
                list.add(newObjects);
            }
        }
        domainPage.setDomains(list);
        return domainPage;
    }

    @Override
    public DomainPage selectSupplierByPage(Map<String, Object> map, long organizationId, int auditStatus, int discountStatus, long pageIndex, long pageSize) {
        DomainPage domainPage = accountRelationDao.selectSupplierByPage(map,organizationId,auditStatus,discountStatus,pageIndex,pageSize);
        List<Object []> list = Lists.newArrayList();
        if(domainPage.getDomains() != null && domainPage.getDomains().size() > 0){
            for(int i = 0;i < domainPage.getDomains().size();i++){
                Object [] objects = (Object[]) domainPage.getDomains().get(i);
                Object [] newObjects = new Object[4];
                long accountRelationId = new BigInteger(objects[0].toString()).longValue();
                long oId = new BigInteger(objects[1].toString()).longValue();
                newObjects[0] = getAccountRelationInfo(accountRelationId);
                newObjects[1] = organizationService.getOrganizationById(oId);
                newObjects[2] = provinceService.getProvinceByID(organizationService.getOrganizationById(oId).getProvinceId());
                newObjects[3] = provinceService.getCityById(organizationService.getOrganizationById(oId).getCityId());
                list.add(newObjects);
            }
        }
        domainPage.setDomains(list);
        return domainPage;
    }

}
