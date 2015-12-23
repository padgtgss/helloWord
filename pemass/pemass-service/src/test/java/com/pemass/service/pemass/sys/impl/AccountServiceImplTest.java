package com.pemass.service.pemass.sys.impl;

import com.pemass.common.server.enumeration.GenderEnum;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.persist.domain.jpa.sys.Account;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.service.pemass.sys.AccountService;
import com.pemass.service.pemass.sys.ConfigService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@ContextConfiguration(locations = {"classpath*:applicationContext.xml",
        "classpath:applicationContext-persist.xml"})
public class AccountServiceImplTest extends AbstractTestNGSpringContextTests {

    @Resource
    private AccountService accountService;

    @Resource
    private ConfigService configService;

    @Test
    public void testRegister() throws Exception {
        //添加系统账户，切记、此账号还未审核，
        Account account = new Account();
        account.setAccountname("huodong@pemass111.com");
        account.setPassword("123456");
        account.setNickname("系统账户");
        account.setGender(GenderEnum.FEMALE);
        Account account1 = accountService.register(account);
        Organization organization = new Organization();
        organization.setOrganizationName("本公司");
        organization.setBusiness("卖冰糕");
        organization.setLicense("/resources/201502/05/2271ac0ac9c94aecabaf2cb0a4f6d3a4.jpeg");
        organization.setTaxCertificate("/resources/201502/05/2271ac0ac9c94aecabaf2cb0a4f6d3a4.jpeg");
        organization.setOrganizationCertificate("/resources/201502/05/2271ac0ac9c94aecabaf2cb0a4f6d3a4.jpeg");
        organization.setLicenseNumber("111111");
        organization.setTaxCertificateNumber("11111");
        organization.setOrganizationCertificateNumber("11111");
        organization.setLegalName("张三");
        organization.setLegalIdcard("5012222");
        organization.setLegalIdcardUrl("/resources/201502/05/2271ac0ac9c94aecabaf2cb0a4f6d3a4.jpeg");
//        organization.setLegalIdcardUrlBack("/resources/201502/05/2271ac0ac9c94aecabaf2cb0a4f6d3a4.jpeg");
        organization.setLocation("四川成都市");
        Province province = new Province();
        province.setId(1L);
//        organization.setProvince(province);
//        City city = new City();
//        city.setId(1L);
////        organization.setCity(city);
//        District district = new District();
//        district.setId(1L);
//        organization.setDistrict(district);
//        organization.setProvince(province);
//        City city = new City();
//        city.setId(1L);
//        organization.setCity(city);
//        District district = new District();
//        district.setId(1L);
//        organization.setDistrict(district);
    }

    @Test
    public void saveConfig() {
//        Config config = new Config();
//        config.setKey(Config.ConfigEnum.COUPON_ACTIVITY.toString());
//        config.setValue("24");
//        config.setTitle("抽奖活动");
//        configService.save(config);
//        Config config1 = new Config();
//        config1.setKey(Config.ConfigEnum.LUCKY_DRAW_ACTIVITY.toString());
//        config1.setValue("24");
//        config1.setTitle("积分卷活动");
//        configService.save(config1);
    }
}