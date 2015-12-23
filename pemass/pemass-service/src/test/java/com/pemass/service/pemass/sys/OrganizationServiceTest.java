/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.AccountRoleEnum;
import com.pemass.persist.enumeration.AuditStatusEnum;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * @Description: OrganizationServiceTest
 * @Author: estn.zuo
 * @CreateTime: 2014-10-15 21:13
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-persist.xml"})
public class OrganizationServiceTest extends AbstractTestNGSpringContextTests {

    @Resource
    private OrganizationService organizationService;

    @Test
    public void testInsert() throws Exception {
    }

    @Test
    public void testUpdate() throws Exception {
       /* Company organization = new Company();
        organization.setId(4L);
        organizationService.update(organization);*/
    }

    @Test
    public void testSelect() throws Exception {
       /* DomainPage domainPage = new DomainPage();
        domainPage.setPageIndex(0);
        domainPage.setPageSize(10);
        organizationService.selectAll(domainPage);*/
    }

    @Test
    public void testSelectByIds() {
        Organization organization = new Organization();
        organization.setOrganizationName("未填写公司名称2");
        organization.setBusiness("未填写经营内容2");
        /**设置默认属性*/
        organization.setAuditStatus(AuditStatusEnum.NONE_AUDIT);//审核状态(默认未审核)
        organization.setAccountRole(AccountRoleEnum.ROLE_SUPPLIER); //账号角色(默认供应商)
        organization.setIsOneMerchant(0);//默认为不是一元购商户
        organization.setOneAuditStatus(AuditStatusEnum.NONE_AUDIT);//一元购积分审核状态(默认未审核)
        organization.setAvailable(AvailableEnum.UNAVAILABLE);
        organization = organizationService.insertOrganization(organization);
        System.out.println(organization.getId() + "-------------------");
        Organization organ = organizationService.getOrganizationById(organization.getId());
        organ.setAvailable(AvailableEnum.UNAVAILABLE);
        organization = organizationService.updateOrganization(organ);
        System.out.println(organization.getAvailable() + "------------------");


    }

    @Test
    public void testSelectIsNotParticularOrganization() {
//        DomainPage domainPage = new DomainPage(10, 0, 0);
//
//        Map<String, Object> map = new HashMap<String, Object>();
////        map.put("organizationName", "是否");
////        Long id = 26L;
////        map.put("provinceId", id);
//        domainPage = organizationService.selectIsNotParticularOrganization(map, domainPage.getPageIndex(), domainPage.getPageSize());
//        System.out.println(domainPage.getDomains().size() + "------------");
//        System.out.println(domainPage.getTotalCount() + "++++++++++++++++++");


    }

    @Test
    public void testGetAllOrganizationAmount() {
        Long amount = organizationService.getAllOrganizationAmount();
        System.out.println("+++++商户总数量:" + amount);

    }


    public static void main(String[] args) {
        /*String phone = "18010558729";

        System.out.println(phone.substring(0,3));
        System.out.println(phone.substring(7,11));*/

//        float num = 0.2f;
//        System.out.println("num * 100 == " + (int) (num * 100));
    }

}
